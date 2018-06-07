package xml_to_vm

import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class ProgramStructure {
    companion object {
        var symbolTable = SymbolTable("")
        var classNAme = ""
        var tab: Int = 0
        @JvmStatic
        var inputFile = ""

        fun classVM() : String{
            var output = ""
            throwNextToken()    // <class>
            throwNextToken()    // class
            classNAme = contentTokenTrim(getNextToken())
            //symbolTable = SymbolTable()
            throwNextToken()    // {
            while (checkNextToken().contains("classVarDec")){
                throwNextToken()    //  <classVarDec>
                classVarDec()
            }
            while (checkNextToken().contains("subroutineDec")){
                throwNextToken()    //  <subroutineDec>
                output += subroutineDec()
            }
            throwNextToken()    //  }
            throwNextToken()    // <class>
            return output
        }

        private fun classVarDec() {
            symbolTable = SymbolTable(classNAme)
            var argKind = kind(contentTokenTrim(getNextToken()))
            var type = contentTokenTrim(getNextToken())
            var name : String
            do {
                name = contentTokenTrim(getNextToken())
                symbolTable.defineClassMap(name, type, argKind)
            }while (checkNextToken().contains(","))
            throwNextToken()    //  ;
            throwNextToken()    // </classVarDec>
        }

        private fun subroutineDec(): String {
            symbolTable.startSubroutine()
            var token = checkNextToken()
            var output = ""
            when {
                token.contains("constructor") -> {
                    //output += "function ${classNAme}.new ${symbolTable.argNum}"
                    var type = contentTokenTrim(getNextToken())
                    var name = contentTokenTrim(getNextToken())
                    throwNextToken()    //  (
                    throwNextToken()    // <parameterList>
                    var num : Int = 0
                    if (!checkNextToken().contains("</parameterList>"))
                        num = numOfParameterList()

                    throwNextToken()    // )
                    output += subroutineBody()

                }
                token.contains("function") -> {

                }
                token.contains("method") -> ""
            }
            return ""
        }

        fun numOfParameterList() : Int{
            var count = 0
            do {
                var type = contentTokenTrim(getNextToken())
                var name = contentTokenTrim(getNextToken())
                symbolTable.defineFunctionMap(name, type, Kind.ARG)
                
                count++

            } while (checkNextToken().contains(","))
            return count
        }
        private fun subroutineBody(): String {
            return ""
        }




        fun getNextToken() : String {

            var s = inputFile.substring(0, inputFile.indexOf("\n")+1)
            var g = inputFile.substring(inputFile.indexOf("\n")+1)
            inputFile = g
            return s.trimStart(' ')
        }

        fun throwNextToken() : String{
            getNextToken()
            return ""
        }

        fun checkNextToken() : String {
            return inputFile.substring(0, inputFile.indexOf("\n")).trimStart(' ')
        }

        fun checkFollow1Token() : String {
            var dump = inputFile.substring(inputFile.indexOf("\n") + 1)
            return dump.substring(0, inputFile.indexOf("\n")).trimStart(' ')
        }

        fun contentTokenTrim(str :String) : String {
            return str.substring(str.indexOf("> ") + 2, str.indexOf(" </"))
        }

        private fun kind(_kind: String) : Kind{
            return when (_kind){
                "static" -> Kind.STATIC
                "field" -> Kind.FIELD
                else -> Kind.NONE
            }
        }
    }
}