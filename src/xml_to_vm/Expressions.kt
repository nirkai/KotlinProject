package xml_to_vm

import xml_to_vm.ProgramStructure.Companion.getNextToken
import xml_to_vm.ProgramStructure.Companion.checkNextToken
import xml_to_vm.ProgramStructure.Companion.throwNextToken

class Expressions {
    companion object {
        fun expression(): String{
            var output = term()
            while (checkNextToken().contains(" \\+ | - | \\* | / | &amp; | \\| | &lt; | &gt; | = ".toRegex())){
                val temp = op()
                throwNextToken()  // throw </term>
                output += term()
                output += temp
            }
            throwNextToken()  // throw </expression>
            return output
        }

        fun term() : String{
            var s = getNextToken()
            var output = when {
                s.contains("integerConstant") -> "push constant ${contentTokenTrim(s)}"
                keyWordConstant(s) -> keyWordConstantToVm(s)
                s.contains("stringConstant") -> stringConstant(s)
                s.contains("(") -> throwNextToken() + // <expression>
                        expression() +
                        throwNextToken()            // )
                s.contains("-|~".toRegex()) -> {
                    var unary = contentTokenTrim(s)
                    throwNextToken()        // <term>
                    term() + unaryVM(unary)
                }
                s.contains("identifier") -> {
                    var temp = getNextToken()
                    when {
                        temp.contains("[") -> varNameArray()
                        temp.contains("\\(|\\.".toRegex()) ->  throwNextToken() +   // <subroutineCall>
                                subroutineCall(s)
                        else -> {
                            "push ${findVarName()}\n"
                        }
                    }
                }
                else -> "error in term"
            }
            throwNextToken()        // </term>
            return output
        }

        private fun findVarName(): String {
            return ""
        }

        fun subroutineNameCallFunction(): String {
            return ""
        }

        fun subroutineNameCallFunctionOutsideClass() : String {
            return ""
        }

        fun expressionList (): String {
            return ""
        }

        fun varNameArray() : String {
            return ""
        }



        fun subroutineCall(s : String) : String {
            val str = contentTokenTrim(s)
            return when (str){
                "(" -> subroutineNameCallFunction()
                "." -> subroutineNameCallFunctionOutsideClass()
                else -> "error in subroutine call"
            }
        }


        fun keyWordConstant(s : String):Boolean{
            return s.contains("true|false|null|this".toRegex())
        }

        fun op () :String {
            var op = getNextToken()
            op = contentTokenTrim(op)
            return when (op) {
                "+" -> "add"
                "-" -> "sub"
                "*" -> "call Math.multiply 2"
                "/" -> "call Math.divide"
                "&amp;" -> "and"
                "|" -> "or"
                "&lt;" -> "lt"
                "&gt;" -> "gt"
                "=" -> "eq"
                else -> "error in op function"
            }
        }

        fun contentTokenTrim(str :String) : String {
            return str.substring(str.indexOf("> ") + 2, str.indexOf(" </"))
        }

        fun keyWordConstantToVm(s : String) : String {
            return when (contentTokenTrim(s)){
                "true" -> "push constant 0\n" + "not\n"
                "false", "null" -> "push constant 0\n"
                "this" -> "push pointer 0\n"
                else -> "error in keyWord constant\n"
            }
        }

        fun stringConstant(str: String):String{
            val s = contentTokenTrim(str)
            var output = "push constant ${s.length}\n" +
                    "call String new\n"
            var temp : Int
            for (i in s){
                temp = i.toInt()
                output += "push constant ${temp}\n" +
                        "call String.appendChar 2\n"
            }
                return output
        }

        fun unaryVM(u : String){
            when (u){
                "~" -> "not\n"
                "-" -> "neg\n"
                else -> "error in unary op\n"
            }
        }
    }
}