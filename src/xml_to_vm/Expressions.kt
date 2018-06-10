package xml_to_vm

import xml_to_vm.ProgramStructure.Companion.checkFollow1Token
import xml_to_vm.ProgramStructure.Companion.getNextToken
import xml_to_vm.ProgramStructure.Companion.checkNextToken
import xml_to_vm.ProgramStructure.Companion.contentTokenTrim
import xml_to_vm.ProgramStructure.Companion.expressionListArgument
import xml_to_vm.ProgramStructure.Companion.symbolTable
import xml_to_vm.ProgramStructure.Companion.throwNextToken
import xml_to_vm.ProgramStructure.Companion.typePopPush

class Expressions {
    companion object {
        fun expression(): String{
            throwNextToken() //<term>
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
            var s = checkNextToken()
            var output = ""
            when {
                s.contains("integerConstant") -> {
                    output +="push constant ${contentTokenTrim(s)}\n"
                    throwNextToken() // <integerConstant></integerConstant>

                }
                keyWordConstant(contentTokenTrim(s)) -> {
                    output +=keyWordConstantToVm(s)
                    throwNextToken() // <keyWordConstant>
                }

                s.contains("stringConstant") -> {
                    output +=stringConstant(s)
                    throwNextToken() // <stringConstants>
                }
                s.contains("(") -> {
                        throwNextToken() // (
                        throwNextToken()  // <expression>
                        output +=expression()
                        throwNextToken()   }         // )
                s.contains("-|~".toRegex()) -> {
                    var unary = contentTokenTrim(s)
                    throwNextToken() // -|~
                    throwNextToken()// <term>
                    output +=term() + unaryVM(unary)
                }
                s.contains("identifier") -> {
                    if (checkFollow1Token().contains("(") or checkFollow1Token().contains(".") )
                        output += subroutineCall()
                    else {
                        var name = contentTokenTrim(s)
                        var type = symbolTable.kindOf(name)
                        var num = symbolTable.indexOf(name)
                        throwNextToken() // varName
                        if (checkNextToken().contains("[")) {
                            throwNextToken() // [
                            throwNextToken() // <expression>
                            var tempOutput = ""
                            tempOutput = expression()
                            throwNextToken() // ]
                            tempOutput += "push ${typePopPush(type)} ${num}\n"
                            tempOutput += "pop poiter 1\n"
                            output += tempOutput
                        } else {
                            output += "push ${typePopPush(type)} ${num}\n"
                        }
                    }
                }

                else -> {
                    throwNextToken() // <subroutineCall>
                    output += subroutineCall()
                    /*var temp = getNextToken()
                    when {
                        temp.contains("[") -> varNameArray()
                        temp.contains("\\(|\\.".toRegex()) -> {
                            throwNextToken()  // <subroutineCall>
                            subroutineCall(s)
                        }
                        else -> {
                            "push ${findVarName()}\n"
                        }
                    }*/
                }
            }
            /*
            var s = getNextToken()
            var output = when {
                s.contains("integerConstant") -> "push constant ${contentTokenTrim(s)}"
                (s) -> keyWordConstantToVm(s)
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
            */
            throwNextToken()        // </term>
            return output
        }

        /*fun subroutineNameCallFunction(): String {
            return ""
        }

        fun subroutineNameCallFunctionOutsideClass() : String {
            return ""
        }*/
        data class Result(val output:String , var listArgument:Int)
        fun expressionList() : Result {
            //expressionListArgument = 0
            var listArg:Int = 0
            var output = ""
            if (!checkNextToken().contains("</expressionList>")) {

                do {
                    //expressionListArgument = expressionListArgument + 1
                    listArg++
                    if (checkNextToken().contains(","))
                        throwNextToken() // ,
                    throwNextToken() // <expression>
                    output += expression()
                } while (checkNextToken().contains(","))
            }
            //throwNextToken() // )
            throwNextToken() // </exprationList>

            return Result(output,listArg)
        }

        fun arrayTemp() : String {
            return ""
        }



        fun subroutineCall() : String {
            var output = ""
            val str = contentTokenTrim(checkFollow1Token())
             when (str){
                "(" -> {
                    output +="push pointer 0\n"
                   var nameFunction = contentTokenTrim(getNextToken())
                    throwNextToken() // (
                    throwNextToken() //<expressionList>
                    var (tmpOutPut,listArg) = expressionList()
                    throwNextToken() // )
                    //var num = expressionListArgument + 1
                    var num = listArg + 1
                    output += tmpOutPut

                    output += "call ${symbolTable.getClassName()}.${nameFunction} ${num}\n"

                }
                "." -> {

                    var name = contentTokenTrim(getNextToken())
                    var kind = symbolTable.kindOf(name)
                    throwNextToken() // .
                    val subroutineName = contentTokenTrim(getNextToken())
                    throwNextToken() // (
                    throwNextToken() // <expressionList>
                    var tmpOutPut =""
                    var num:Int
                    if (symbolTable.typeOf(name).equals("")){ // ClassName
                        //throwNextToken() // <expressionList>
                        var (tmpOut, listArg) = expressionList()
                        throwNextToken() // )
                        num = listArg
                        tmpOutPut += tmpOut
                    }
                    else{ // varName

                        output += "push ${typePopPush(kind)} 0\n"
                        //throwNextToken() //<expressionList>
                        var (tmpOut,listArg) = expressionList()
                        throwNextToken() // )
                        //num = expressionListArgument + 1
                        num = listArg + 1
                        tmpOutPut += tmpOut
                    }
                    output += tmpOutPut
                    var type = symbolTable.typeOf(name)
                    if ( type.equals(""))
                        output += "call ${name}.${subroutineName} ${num}\n"
                    else
                        output += "call ${symbolTable.typeOf(name)}.${subroutineName} ${num}\n"
                }
                else -> "error in subroutine call"
            }
            if (checkNextToken().equals("</subroutioneCall>") )
                throwNextToken() // </subroutionCall>
            return output
        }


        fun keyWordConstant(s : String):Boolean{
            return s.contains("true|false|null|this".toRegex())
        }

        fun op () :String {
            var op = getNextToken()
            op = contentTokenTrim(op)
            return when (op) {
                "+" -> "add\n"
                "-" -> "sub\n"
                "*" -> "call Math.multiply 2\n"
                "/" -> "call Math.divide 2\n"
                "&amp;" -> "and\n"
                "|" -> "or\n"
                "&lt;" -> "lt\n"
                "&gt;" -> "gt\n"
                "=" -> "eq\n"
                else -> "error in op function\n"
            }
        }

        fun keyWordConstantToVm(s : String) : String {
             val output = when (contentTokenTrim(s)){
                "true" -> "push constant 0\n" + "not\n"
                "false", "null" -> "push constant 0\n"
                "this" -> "push pointer 0\n"
                else -> "error in keyWord constant\n"
            }
            return output
        }

        fun stringConstant(str: String):String{
            val s = contentTokenTrim(str)
            var output = "push constant ${s.length}\n" +
                    "call String.new 1\n"
            var temp : Int
            for (i in s){
                temp = i.toInt()
                output += "push constant ${temp}\n" +
                        "call String.appendChar 2\n"
            }
                return output
        }

        fun unaryVM(u : String):String{
           return when (u){
                "~" -> "not\n"
                "-" -> "neg\n"
                else -> "error in unary op\n"
            }
        }
    }
}