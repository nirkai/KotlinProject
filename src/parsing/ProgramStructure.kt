package parsing

import jack_to_vm.Tokenizing
import java.io.File
import java.io.FileWriter

class ProgramStructure {
    companion object {
        var inputFile : String = ""

        fun tokenizing() {

            var filePath = System.getProperty("user.dir")
            filePath += "//out//test"
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

        fun classT() : String {
            getNextToken()
            var output = "<class>\n" +
                    getNextToken() + getNextToken() + getNextToken()
            while (checkNextToken().contains("static|field".toRegex())){
                output += "<classVarDec>"

                output += "</classVarDec>"
            }

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

        fun parseClassVarDec(){

        }
    }
}