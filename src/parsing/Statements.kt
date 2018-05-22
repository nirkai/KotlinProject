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
            var stat = incTab() + "<letStatement>\n" + getNextToken() + getNextToken()     // let varName
            if (checkNextToken().contains(" [ "))
                stat += getNextToken() + expression() + getNextToken()  //  [ expression ]
            stat += getNextToken() + expression() + getNextToken()      // = expression ;
            return stat + decTab() + "</letStatement>\n"
        }

        fun ifStatement() : String {
            var stat = incTab() + "<ifStatement>\n" + tempIfWhile()
            if (checkNextToken().contains("else"))
                stat += getNextToken() + getNextToken() + statements() + getNextToken()    //  else { statement }

            return stat + decTab() + "</ifStatement>\n"
        }

        fun whileStatement() : String{
            return incTab() + "<whileStatement>\n" + tempIfWhile() + decTab() + "</whileStatement>\n"
        }

        fun tempIfWhile() : String{
            return getNextToken() +
                    getNextToken() + expression() + getNextToken() +    //  ( expression )
                    getNextToken() + statements() + getNextToken()    //  { statements }
        }

        fun doStatement() : String {
            return incTab() + "<doStatement>\n" + getNextToken() + subroutineCall() + getNextToken() + decTab() + "</doStatement>\n"  // do subroutine ;
        }

        fun returnStatement() : String {
            var stat = incTab() + "<returnStatement>\n" + getNextToken()
            if (!checkNextToken().contains(" ; "))
                stat += expression()
            return stat + getNextToken() + decTab() + "</returnStatement>\n"
        }

    }
}