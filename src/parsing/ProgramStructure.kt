package parsing

import java.io.File

class ProgramStructure {
    companion object {
        var inputFile : String = ""

        fun tokenizing() {

            var filePath = System.getProperty("user.dir")
            filePath += "//test"
            File(filePath).walk().forEach { fileJack ->
                if (fileJack.isFile && fileJack.name.contains("T.xml")) {
                    inputFile = fileJack.readText()


                    /*val lineList = mutableListOf<String>()
                    var hackProgramString = ""

                    fileJack.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
                    lineList.forEach{
                        var s = it.replace(" *<.*?> *".toRegex(), "")
                        println(s)
                    }*/


                   /* var outStream = FileWriter(filePath + "\\" + fileJack.name.removeSuffix("T.xml") + ".xml")
                    outStream.write("<tokens>\n")
                    var output = Tokenizing.jackToTok(fileJack)
                    outStream.append(output + "</tokens>\n")
                    outStream.close()*/
                }
            }
        }

        fun parseClass() : String {
            getNextToken()
            var output = "<class>\n" +
                    getNextToken() + getNextToken() + getNextToken()
            output += parseClassVarDec()


            output += getNextToken()
            output += "</class>"
            return  output
        }

        fun getNextToken() : String {
            var s = inputFile.substring(0, inputFile.indexOf("\n"))
            inputFile = inputFile.substring(inputFile.indexOf("\n"))
            return s
        }

        fun checkNextToken() : String {
            return inputFile.substring(0, inputFile.indexOf("\n"))
        }

        fun parseClassVarDec() : String{
            var output = ""
            while (checkNextToken().contains("static|field".toRegex())){
                output += "<classVarDec>"
                output += getNextToken() + getNextToken() + getNextToken()
                while (checkNextToken().contains(",")){
                    output += getNextToken() + getNextToken()
                }
                output += getNextToken() + "</classVarDec>"
            }
            return output
        }

        fun subroutineDec(){

        }

        fun expression() : String{
            var output = "<expression>\n"
            output += term()
            while (checkNextToken().contains("\\+|-|\\*|/|&|\\||<|>|=".toRegex())){
                output += getNextToken()
                output += term()
            }
            output += "</expression>\n"
            return output
        }

        fun term () :String{
            var s = checkNextToken()
            var output = when {
                s.contains("integerConstant|stringConstant".toRegex()) -> getNextToken()
                s.contains("(") -> getNextToken() + expression() + getNextToken()
                keyWordConstant() -> getNextToken()
                unaryOp() -> getNextToken() + term()
                else -> {
                        var ident = getNextToken()
                        if (ident.contains("[")) {
                                ident += getNextToken()
                            ident += expression()
                            ident += getNextToken() // ) or ]
                            ident
                        }
                        else {
                            ident += subroutineCall()
                            ident
                        }
                    }
                }
            return output
        }

        fun keyWordConstant():Boolean{
            return checkNextToken().contains("true|false|null|this".toRegex())
        }
/*

        fun varName():Boolean{
            return isIdentifier()
        }

        fun className():Boolean{
            return isIdentifier() && checkNextToken().contains("class")
        }

        fun subroutineName() : Boolean {
            return isIdentifier()
        }
*/

        fun subroutineCall() : String {
            var ident = getNextToken()
            return when {
                checkNextToken().contains("(") -> {     // subroutineName
                    ident +
                            getNextToken() +    // (
                            expressionList() +  // expressionList
                            getNextToken()      // )

                }
                checkNextToken().contains(".") -> ident + getNextToken() + getNextToken() +           //  className | varName
                        getNextToken() + expressionList() + getNextToken()  //  className | varName . subroutineName ( expressionList )
                else -> ""
            }
        }

        fun isIdentifier() : Boolean {
            return checkNextToken().contains("identifier")
        }

        fun unaryOp() : Boolean {
            return checkNextToken().contains("-|~".toRegex())
        }

        fun expressionList() : String{
            var expr = expression()         // can be empty. the empty content will return from the subroutineCall function
            while (checkNextToken().contains(",")){
                expr += getNextToken() + expression()
            }
            return expr
        }
    }
}