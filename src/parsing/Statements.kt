package parsing


import parsing.Expressions.Companion.expression
import parsing.Expressions.Companion.subroutineCall
import parsing.ProgramStructure.Companion.checkNextToken
import parsing.ProgramStructure.Companion.getNextToken
import parsing.ProgramStructure.Companion.decTab
import parsing.ProgramStructure.Companion.incTab

class Statements {
    companion object {
        fun statements() :String{
            var stat = incTab()
            var tmp = statement()
            if (tmp != "")
                stat += "<statements>\n"
            while (tmp != ""){
                stat += tmp
                tmp = statement()
            }
            if (stat != "")
                stat += decTab() + "</statements>\n"
            return stat
        }

        fun statement() : String{
            val stat = checkNextToken()
            val x = when {
                stat.contains("let") -> letStatement()
                stat.contains("if") -> ifStatement()
                stat.contains("while") -> whileStatement()
                stat.contains("do") -> doStatement()
                stat.contains("return") -> returnStatement()
                else -> ""
            }
            return x
        }

        fun letStatement() :String {
            var stat = incTab("letStatement") + getNextToken() + getNextToken()     // let varName
            if (checkNextToken().contains(" [ "))
                stat += getNextToken() + expression() + getNextToken()                  //  [ expression ]
            stat += getNextToken() + expression() + getNextToken()                      //  = expression ;
            return stat + decTab("letStatement")
        }

        fun ifStatement() : String {
            var stat = incTab("ifStatement") + tempIfWhile()
            if (checkNextToken().contains("else"))
                stat += getNextToken() + getNextToken() + statements() + getNextToken()    //  else { statement }
            return stat + decTab("ifStatement")
        }

        fun whileStatement() : String{
            return incTab("whileStatement") + tempIfWhile() + decTab("whileStatement")
        }

        fun tempIfWhile() : String{
            return getNextToken() +                                     // if | while
                    getNextToken() + expression() + getNextToken() +    //  ( expression )
                    getNextToken() + statements() + getNextToken()      //  { statements }
        }

        fun doStatement() : String {
            return incTab("doStatement") +
                    getNextToken() + subroutineCall() + getNextToken() +   // do subroutine ;
                    decTab("doStatement")
        }

        fun returnStatement() : String {
            var stat = incTab("returnStatement") +
                    getNextToken()                          // return
            if (!checkNextToken().contains(" ; "))
                stat += expression()                        // expression ?
            return stat + getNextToken() +                  // ;
                    decTab("returnStatement")
        }

    }
}