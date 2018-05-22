package jack_to_vm

import java.io.File
import java.io.FileWriter
import java.io.IOException

class Tokenizing {
    companion object {
        fun tokenizing() {
            //var str = "father went /* blabla */ to work"
            //str = str.replace("/\\*.*?\\*/".toRegex(), "")
            //str = str.replaceRange(str.indexOf("//")..str.indexOf("\n"), "")
            //println(str)

            var filePath = System.getProperty("user.dir")
            filePath += "//test"
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
            var content = str
            content = content.replace("//.*?\r\n".toRegex(), "\r\n")
            content = content.replace("/\\*.*?\\*/".toRegex(), "\r\n")
            content = content.replace("\r\n"," ")
            content = content.replace("  *".toRegex(), " ")
            println(content)

            fun number(index : Int) : Int{
                var i = index + 1
                while (i < content.length && content[i].isDigit())
                        word += content[i++].toString()
                return i - 1
            }

            fun stringConstant(index : Int) : Int{
                var i = index + 1
                while (i < content.length && !content[i].equals('\"'))
                    word += content[i++].toString()
                return i
            }

            /*fun keyWords(index : Int) : Int{
                var i = index + 1
                word = content[index].toString()
                while (i < content.length && TokensWords.keywordsList.forEach { keyword ->
                            keyword.startsWith(word)
                        } != null)
                    word += content[i++].toString()
                word = word.substring(0, word.length - 1)
                if (TokensWords.keywordsList.forEach { keyword ->
                            keyword.equals(word)
                        } != null )
                    return i - 1
                else
                    return index
            }*/
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

            fun comment1(index : Int): Int {
               /* var i = index + 1
                while ((i < content.length && !content[i].equals('\n')) || content[i].equals(' ')){
                    println(content[i])
                    i++
                }
                while (content[i].equals(' ') || content[i].equals('\r') || content[i].equals('\n')) i++;
                var s = content[i]
                println(content[i] + " " + content[i+1])
                if (content[i].equals('/') && content[i+1].equals('/'))
                    comment1(i)
                return i*/
                return content.indexOf('\n', index)
            }

            fun comment2(index : Int): Int {
                /*var i = index + 1
                while ((i < content.length && !(content[i].equals('*') && content[i+1].equals('/'))) || content[i].equals(' ')){
                    if (content[i+1].equals('*') && content[i+2].equals('/'))
                        i
                    i++
                }
                while (content[i].equals(' ') || content[i].equals('\r')  || content[i].equals('\n')) i++;
                if (content[i].equals('/') && content[i+1].equals('/'))
                    comment1(i)
                if (content[i].equals('/') && content[i+1].equals('*'))
                    comment2(i)
                return i*/
                return content.indexOf("*/", index) + 1
            }

            var i = 0
            try {
                while (i < content.length) {
                    if (content[i].equals(' ')) {
                    } else if (content.get(i).isDigit()) {
                        word += content[i].toString()
                        i = number(i)
                        output += "<integerConstant> $word </intergerConstant>\n"
                        word = ""
                    } else if (TokensWords.symbolList.contains(content[i])) {
                        output += "<symbol> ${symbol(content[i])} </symbol>\n"
                    } else if (content[i].equals('\"')) {
                        i = stringConstant(i)
                        output += "<StringConstant> $word </StringConstant>\n"
                        word = ""
                    } else {
                        TokensWords.keywordsList.forEach { keyword ->

                            if (word.equals("") && keyword.startsWith(content[i])) {
                                word = keyWords(i, content, "", keyword)
                            }
                        }
                        if (!word.equals("")) {
                            i += word.length - 1
                            output += "<keyword> $word </keyword>\n"
                            word = ""
                        } else if (content[i].isLetter() || content[i].equals('_')) {
                            while (i < content.length && !content[i].equals(' ') && !isSymbolExist(content[i])) {
                                word += content[i]
                                i++
                            }
                            output += "<identifier> $word </identifier>\n"
                            word = ""
                            i--
                        }

                    }
                    i++
                }
            }
            catch (e : IOException){
                println(content.substring(i))
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
    }


}

