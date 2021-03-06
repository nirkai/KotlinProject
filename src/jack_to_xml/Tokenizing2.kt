package jack_to_xml

import java.io.File
import java.io.FileWriter
import java.util.regex.Pattern

class Tokenizing2 {
    companion object {
        fun tokenizing(filePath : String) {

            File(filePath).walk().forEach { fileJack ->
                if (fileJack.isFile && fileJack.name.contains(".jack")) {
                    var outStream = FileWriter(filePath + "\\" + fileJack.name.removeSuffix(".jack") + "T.xml")
                    outStream.write("<tokens>\n")
                    var output = jackToTok(fileJack)
                    outStream.append(output + "</tokens>\n")
                    outStream.close()
                }
            }
        }

        fun jackToTok(fileJack: File): String {
            var output = ""
            var word: String = "";
            val str = fileJack.readText()
            println(str  + "\n\n")

            var content = str
            content = content.replace("//.*?\r?\n".toRegex(), "\n")
            content = content.replace("/\\*(?s).*?\\*/".toRegex(), "\n")
            content = content.replace("\r?\n".toRegex()," ")
            content = content.replace("\t".toRegex()," ")
            //content = content.replace("  *".toRegex(), " ")
            println(content)

            var d = "3123; jljl"
            var p = Pattern.compile("[0123456789]*")
            var m = p.matcher(d)
            if (m.find())
                println(m.group(0))

            fun number() : String {
                var w = ""
                while (content.isNotEmpty() && content[0].isDigit()) {
                    w += content[0].toString()
                    content = content.substring(1)
                }
                return w
            }

            fun stringConstant() : String {
                content = content.substring(1)
                var s = content.substring(0,content.indexOf('\"'))
                return s
            }

            fun keyWords(index: Int, c: String, w1:String, w2:String):String{
                if (w1.equals(w2) && (c[index].equals(' ') || isSymbolExist(c[index])))
                    return w2
                else if(w1.length < w2.length && w2.startsWith(w1)) {
                    var w = w1
                    w += c[index]
                    w = keyWords(index + 1, c, w, w2)
                    return w
                }
                return ""
            }

            while (content.isNotEmpty()){
                when {
                    content[0].equals(' ') ->
                        content = content.trimStart(content[0])

                    content[0].isDigit() -> {
                            val n = number()
                        output += tokBuild("integerConstant", n)
                    }
                    TokensWords.symbolList.contains(content[0]) -> {
                        output += tokBuild("symbol", symbol(content[0]))
                        content = content.substring(1)
                    }
                    content[0].equals('\"') -> {
                        val w = stringConstant()
                        output += tokBuild("stringConstant", w)
                        content = content.substring(w.length + 1)
                        word = ""
                    }
                    else -> {
                        var w = ""
                        while (content.isNotEmpty() && !content[0].equals(' ') && !isSymbolExist(content[0])){
                            w += content[0]
                            content = content.substring(1)

                        }
                        if (TokensWords.keywordsList.contains(w)){
                            output += tokBuild("keyword", w)
                        }
                        else{
                            output += tokBuild("identifier", w)
                        }


                        /*TokensWords.keywordsList.forEach { keyword ->

                            if (w.equals("") && keyword.startsWith(content[0])) {
                                w = keyWords(0, content, "", keyword)
                            }
                        }
                        if (!w.equals("")) {
                            output += "<keyword> $w </keyword>\n"
                            for (w1 in w)
                                content = content.trimStart(w1)
                            word = ""
                        } else if (content[0].isLetter() || content[0].equals('_')) {
                            var w = ""
                            while (content.isNotEmpty() && !content[0].equals(' ') && !isSymbolExist(content[0])) {
                                w += content[0]
                                content = content.substring(1)
                            }
                            output += "<identifier> $w </identifier>\n"
                            for (w1 in w)
                                content = content.trimStart(w1)
                            word = ""

                        }*/

                    }
                }
            }

            return output
        }

        fun isSymbolExist(symbol: Char) : Boolean{
            TokensWords.symbolList.forEach {
                if (it.equals(symbol))
                    return true
            }
            return false
        }

        fun symbol(s : Char) : String{
            return when (s){
                    '>' -> "&gt;"
                    '<' -> "&lt;"
                    '&' -> "&amp;"
                    '"' -> "&quet;"
                    else -> s.toString()
                }
        }

        fun tokBuild(tok : String, cont : String) :String{
            return "<$tok> $cont </$tok>\n"
        }

    }


}

