package parsing


import parsing.Expressions.Companion.expression
import parsing.Expressions.Companion.subroutineCall
import parsing.ProgramStructure.Companion.checkNextToken
import parsing.ProgramStructure.Companion.getNextToken

class Statements {
    companion object {
        fun statments() :String{
            var stat = ""
            var tmp = statment()
            if (tmp != "")
                stat += "<statments>\n"
            while (tmp != ""){
                stat += tmp
                tmp = statment()
            }
            if (stat != "")
                stat += "</statments>\n"
            return stat
        }

        fun statment() : String{
            val stat = checkNextToken()
            val x = when {
                stat.contains("let") -> letStatment()
                stat.contains("if") -> ifStatment()
                stat.contains("while") -> whileStatment()
                stat.contains("do") -> doStatment()
                stat.contains("return") -> returnStatment()
                else -> ""
            }
            return x
        }

        fun letStatment() :String {
            var stat = "<letStatment>\n" + getNextToken() + getNextToken()
            if (checkNextToken().equals("["))
                stat += getNextToken() + expression() + getNextToken()  //  [ expression ]
            stat += getNextToken() + expression() + getNextToken()      // = expression ;
            return stat
        }

        fun ifStatment() : String {
            var stat = "<ifStatment>\n" + tempIfWhile()
            if (checkNextToken().contains("else"))
                stat += getNextToken() + getNextToken() + statments() + getNextToken() +    //  else { statment }
                        "</ifStatment>\n"
            return stat
        }

        fun whileStatment() : String{
            return "<whileStatment>\n" + tempIfWhile() + "</whileStatment>\n"
        }

        fun tempIfWhile() : String{
            return getNextToken() +
                    getNextToken() + expression() + getNextToken() +    //  ( expression )
                    getNextToken() + statments() + getNextToken()    //  { statments }
        }

        fun doStatment() : String {
            return "<doStatment>\n" + getNextToken() + subroutineCall() + getNextToken() + "/<doStatment>\n"
        }

        fun returnStatment() : String {
            var stat = "<returnStatment>\n" + getNextToken()
            if (checkNextToken().contains(";"))
                return stat + getNextToken() + "</returnStatment>\n"
            return stat + expression() + getNextToken() + "</returnStatment>\n"
        }

    }
}