package parsing

class Expressions {
    companion object {
        fun expression() : String{
            var output = "<expression>\n"
            output += term()
            while (ProgramStructure.checkNextToken().contains("\\+|-|\\*|/|&|\\||<|>|=".toRegex())){
                output += ProgramStructure.getNextToken()
                output += term()
            }
            output += "</expression>\n"
            return output
        }

        fun term () :String{
            var s = ProgramStructure.checkNextToken()
            var output = when {
                s.contains("integerConstant|stringConstant".toRegex()) -> ProgramStructure.getNextToken()
                s.contains("(") -> ProgramStructure.getNextToken() + expression() + ProgramStructure.getNextToken()
                keyWordConstant() -> ProgramStructure.getNextToken()
                unaryOp() -> ProgramStructure.getNextToken() + term()
                isIdentifier() -> {
                    var ident = ProgramStructure.getNextToken()
                    if (ProgramStructure.checkNextToken().contains("[")) {
                        ident += ProgramStructure.getNextToken()
                        ident += expression()
                        ident += ProgramStructure.getNextToken() // ]
                        ident
                    }
                    else {
                        ident += subroutineCall()
                        ident
                    }
                }
                else -> ""
            }
            return output
        }

        fun keyWordConstant():Boolean{
            return ProgramStructure.checkNextToken().contains("true|false|null|this".toRegex())
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
            var ident = ""
            var x = when {
                ProgramStructure.checkNextToken().contains("(") -> {     // subroutineName
                    ident +
                            ProgramStructure.getNextToken() +    // (
                            expressionList() +  // expressionList
                            ProgramStructure.getNextToken()      // )

                }
                ProgramStructure.checkNextToken().contains(".") -> ident + ProgramStructure.getNextToken() + ProgramStructure.getNextToken() +           //  className | varName
                        ProgramStructure.getNextToken() + expressionList() + ProgramStructure.getNextToken()  //  className | varName . subroutineName ( expressionList )
                else -> ""
            }
            if (x != "")
                return label("subroutineCall", x)
            return x
        }

        fun isIdentifier() : Boolean {
            return ProgramStructure.checkNextToken().contains("identifier")
        }

        fun unaryOp() : Boolean {
            return ProgramStructure.checkNextToken().contains("-|~".toRegex())
        }

        fun expressionList() : String{
            var expr = expression()         // can be empty. the empty content will returned from the subroutineCall function
            while (expr != "" && ProgramStructure.checkNextToken().contains(",")){
                expr += ProgramStructure.getNextToken() + expression()
            }
            if (expr != ""){
                return label("expressionList", expr)
            }
            return expr
        }

        fun label(tok : String, cont : String) : String {
            return "<$tok>\n $cont </$tok>\n"
        }
    }
}