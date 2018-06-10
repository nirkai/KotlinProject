package xml_to_vm

import xml_to_vm.Statements.Companion.statements
import java.io.File
import java.io.FileWriter
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class ProgramStructure {
    companion object {
        var symbolTable = SymbolTable("")
        var classNAme = ""

        @JvmStatic var inputFile = ""

        var expressionListArgument:Int = 0



        fun tokenizing(filePath : String) {


            File(filePath).walk().forEach { fileJack ->
                if (fileJack.isFile && fileJack.name.contains(".xml")) {
                    inputFile = fileJack.readText()
                    var g = fileJack.readText()

                    var outStream = FileWriter(filePath + "\\" + fileJack.name.removeSuffix(".xml") + ".vm")
                    outStream.write(classVM())
                    outStream.close()
                }
            }
        }
        fun classVM() : String{
            var output = ""
            throwNextToken()    // <class>
            throwNextToken()    // class
            classNAme = contentTokenTrim(getNextToken())
            symbolTable = SymbolTable(classNAme)
            symbolTable.nameOfClass = classNAme

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
            //symbolTable = SymbolTable(classNAme)
            var argKind = kind(contentTokenTrim(getNextToken()))
            var type = contentTokenTrim(getNextToken())
            var name : String
            do {
                if (checkNextToken().contains(","))
                    throwNextToken() // ,
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
            var num : Int = 0
            when {
                token.contains("constructor") -> {

                    throwNextToken() // constructor
                    throwNextToken() // type
                    throwNextToken() // sunboutinName
                    throwNextToken()    //  (
                    throwNextToken()    // <parameterList>


                    if (!checkNextToken().contains("</parameterList>"))
                        parameterList()
                    else
                        throwNextToken() // </parameterList

                    throwNextToken()    // )
                    throwNextToken() // <subrotineBody>
                    var tempOutput = subroutineBody()

                    output += "function ${symbolTable.getClassName()}.new ${symbolTable.varCount(Kind.VAR)}\n" +
                            "push constant ${symbolTable.varCount(Kind.FIELD)}\n" +
                            "call Memory.alloc 1\n" +
                            "pop pointer 0\n" +
                            tempOutput/* +
                            "push pointer 0\n" +
                            "return\n"*/



                }
                token.contains("function") -> {
                    throwNextToken() // function
                    throwNextToken() // type
                    var name = contentTokenTrim(getNextToken())
                    throwNextToken()    //  (
                    throwNextToken()    // <parameterList>

                    if (!checkNextToken().contains("</parameterList>"))
                        parameterList()
                    else
                        throwNextToken() // </parameterList>
                    throwNextToken()    // )
                    throwNextToken() // <subrotineBoby>
                    var tempOutput = subroutineBody()

                    output += "function ${symbolTable.getClassName()}.${name} ${symbolTable.varCount(Kind.VAR)}\n" +
                            tempOutput
                }
                token.contains("method") ->{
                    throwNextToken() // method
                    var type = contentTokenTrim(getNextToken())
                    var name = contentTokenTrim(getNextToken())
                    throwNextToken()    //  (
                    throwNextToken()    // <parameterList>
                    symbolTable.defineFunctionMap("this", symbolTable.nameOfClass,Kind.ARG)
                    if (!checkNextToken().contains("</parameterList>")) {
                        //throwNextToken() // <parameterList>
                        parameterList()
                    }
                    else
                        throwNextToken() // </parameterList>
                    throwNextToken()    // )
                    throwNextToken() // <subrotineBoby>
                    var tempOutput = subroutineBody()

                    output += "function ${symbolTable.getClassName()}.${name} ${symbolTable.varCount(Kind.VAR)}\n" +
                            "push argument 0\n"  + //this
                            "pop pointer 0\n" +
                            tempOutput

                }
            }
            throwNextToken() // </subroutineDec>
            return output
        }

        fun parameterList() {
            //var count = num

            do {
                if (checkNextToken().contains(","))
                    throwNextToken() // ,
                var type = contentTokenTrim(getNextToken())
                var name = contentTokenTrim(getNextToken())
                symbolTable.defineFunctionMap(name, type, Kind.ARG)
                //count++
            } while (checkNextToken().contains(","))
            throwNextToken() // </parameterList>

        }
        private fun subroutineBody(): String {
            var output = ""
            throwNextToken() // {
            do {
                if (checkNextToken().contains("varDec")) {
                    throwNextToken() // <varDec>
                    varDec();
                }
            }while (checkNextToken().contains("var"))

            throwNextToken() // <statements>
            output += statements();
            throwNextToken() // </subroutineBody>
            return output
        }



        private fun varDec() {
            throwNextToken() // var
            var type = contentTokenTrim(getNextToken())
            var name = ""
            do{
                if (checkNextToken().contains(","))
                    throwNextToken() // ,

                name = contentTokenTrim(getNextToken())
                symbolTable.defineFunctionMap(name,type,Kind.VAR)
            }while (checkNextToken().contains(","))
            throwNextToken()// ;
            throwNextToken() // varDec
        }


        fun getNextToken() : String {

            var s = inputFile.substring(0, inputFile.indexOf("\n")+1)
            var g = inputFile.substring(inputFile.indexOf("\n")+1)
            inputFile = g
            return s.trimStart(' ')
        }

        fun throwNextToken() {
            getNextToken()
            //return ""
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
        fun typePopPush(type:Kind):String{
            return when{
                type.equals(Kind.STATIC) ->  "static"
                type.equals(Kind.FIELD) ->  "this"
                type.equals(Kind.VAR) -> "local"
                type.equals(Kind.ARG) ->  "argument"
                else ->" error from function letStetment"
            }
        }
    }
}