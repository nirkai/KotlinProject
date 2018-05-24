package parsing

import parsing.Statements.Companion.statements
import java.io.File
import java.io.FileWriter

class ProgramStructure {

    companion object {
        var tab : Int = 0
        @JvmStatic var inputFile = ""

        fun tokenizing() {

            var filePath = System.getProperty("user.dir")
            filePath += "//test"
            File(filePath).walk().forEach { fileJack ->
                if (fileJack.isFile && fileJack.name.contains("T.xml")) {
                    inputFile = fileJack.readText()
                    var g = fileJack.readText()

                    /*val lineList = mutableListOf<String>()
                    var hackProgramString = ""

                    fileJack.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
                    lineList.forEach{
                        var s = it.replace(" *<.*?> *".toRegex(), "")
                        println(s)
                    }*/


                    var outStream = FileWriter(filePath + "\\" + fileJack.name.removeSuffix("T.xml") + ".xml")
                    outStream.write(parseClass())
                    outStream.close()
                }
            }
        }

        fun parseClass() : String {
            //tab = 0
            getNextToken()
            //tab++
            var output = incTab("class") +
                    getNextToken() + getNextToken() + getNextToken()
            output += parseClassVarDec()

            while (checkNextToken().contains("method|function|constructor".toRegex()) && !inputFile.equals("</tokens>\n")){
                output += subroutineDec()
            }
            output += getNextToken()
            output += decTab("class")
            return  output
        }

        fun getNextToken() : String {

            var s = inputFile.substring(0, inputFile.indexOf("\n")+1)
            var g = inputFile.substring(inputFile.indexOf("\n")+1)
            inputFile = g
            return space.repeat(tab) + s
        }

        fun checkNextToken() : String {

            return inputFile.substring(0, inputFile.indexOf("\n"))
        }

        fun checkFollow1Token() : String {

            var dump = inputFile.substring(inputFile.indexOf("\n") + 1)
            return dump.substring(0, inputFile.indexOf("\n"))
        }

        fun parseClassVarDec() : String{
            var output = ""
            while (checkNextToken().contains("static|field".toRegex()) && !inputFile.equals("</tokens>\n")){
                output += incTab("classVarDec")
                output += getNextToken() + getNextToken() + getNextToken()
                while (checkNextToken().contains(",")&& !inputFile.equals("</tokens>\n")){
                    output += getNextToken() + getNextToken()
                }
                output += getNextToken() +      // ;
                        decTab("classVarDec")
            }
            return output
        }

        fun subroutineDec() :String {
            var output = incTab("subroutineDec")
                output += getNextToken() + getNextToken() + getNextToken() +     // method|function|constructor void|type subroutineName
                    getNextToken() + parameterList() + getNextToken() +          // ( parameterList )
                    subroutineBody() + decTab("subroutineDec")
            return output
        }

        fun parameterList() :String{
            var parm = incTab("parameterList")
            if (checkNextToken().contains("void|int|boolean|char|identifier".toRegex())){
                parm += getNextToken() + getNextToken()     //  type varName
                while (checkNextToken().contains(","))
                    parm += getNextToken() + getNextToken() + getNextToken()    //  , type varName
            }
            return parm + decTab("parameterList")
        }

        fun subroutineBody():String{
            var body = incTab("subroutineBody") + getNextToken()    //  {
            while (checkNextToken().contains("var"))
                body += varDec()
            body += statements()
            body += getNextToken() + decTab("subroutineBody")
            return body
        }

        fun varDec() :String{
            var dec = incTab("varDec") + getNextToken() + getNextToken() + getNextToken()  //  var type varName
            while (checkNextToken().contains(","))
                dec += getNextToken() + getNextToken()                  // , varName
            dec += getNextToken() + decTab("varDec")               // ;
            return dec
        }

        /*fun label(tok : String, cont : String) : String {
            return "${incTab()}<$tok>\n $cont ${decTab()}</$tok>\n"
        }*/

        fun incTab() = space.repeat(tab++)

        fun incTab(str : String) = "${space.repeat(tab++)}<$str>\n"

        fun decTab() = space.repeat(--tab)

        fun decTab(str : String) = "${space.repeat(--tab)}</$str>\n"

        private var space = "  "
    }
}