package parsing

import parsing.ProgramStructure.Companion.checkFollow1Token
import parsing.ProgramStructure.Companion.checkNextToken
import parsing.ProgramStructure.Companion.decTab
import parsing.ProgramStructure.Companion.getNextToken
import parsing.ProgramStructure.Companion.incTab

class Expressions {
    companion object {
        fun expression() : String{
            var output = incTab("expression")
            output += term()
            while (checkNextToken().contains(" \\+ | - | \\* | / | &amp; | \\| | &lt; | &gt; | = ".toRegex())){
                output += getNextToken()    // op
                output += term()
            }
            return output + decTab("expression")
        }

        fun term () :String{
            var s = checkNextToken()
            var output = incTab("term") +
                    when {
                s.contains("integerConstant|stringConstant".toRegex()) -> getNextToken()
                s.contains("(") -> getNextToken() + expression() + getNextToken()
                keyWordConstant() -> getNextToken()
                unaryOp() -> getNextToken() + term()
                isIdentifier() -> {
                    var ident = ""
                    if (checkFollow1Token().contains("[")) {
                        ident += getNextToken() + getNextToken()    // varName [
                        ident += expression()                       //  expression
                        ident += getNextToken()                     // ]
                        ident
                    }
                    else {
                        ident += subroutineCall()
                        ident
                    }
                }
                else -> ""
            }
            if (output.equals("<term>\n"))
                return ""
            return output + decTab("term")
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
            var s = checkNextToken()
            ident += when {
                s.contains(" ( ") -> {     // subroutineName
                            getNextToken() +    // (
                            expressionList() +  // expressionList
                            getNextToken()      // )

                }
                s.contains(".") -> getNextToken() + getNextToken() +           //  className | varName
                        getNextToken() + expressionList() + getNextToken()  //  className | varName . subroutineName ( expressionList )
                else -> ""
            }
            /*if (x != "")
                return label("subroutineCall", x)*/
            return ident
        }

        fun isIdentifier() : Boolean {
            return checkNextToken().contains("identifier")
        }

        fun unaryOp() : Boolean {
            return checkNextToken().contains("-|~".toRegex())
        }

        fun expressionList() : String{
            if (checkNextToken().contains(" ) "))
                return label("expressionList", "")
            incTab()
            var expr = expression()
            while (expr != "" && checkNextToken().contains(",")){
                expr += getNextToken() + expression()
            }
            if (expr != ""){
                decTab()
                expr = incTab("expressionList") + expr + decTab("expressionList")
                //return label("expressionList", expr)
            }
            return expr
        }

        fun label(tok : String, cont : String) : String {
            return "${incTab(tok)}$cont${decTab(tok)}"
        }
    }
}