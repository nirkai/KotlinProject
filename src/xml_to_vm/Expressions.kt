package xml_to_vm

import xml_to_vm.ProgramStructure.Companion.checkFollow1Token
import xml_to_vm.ProgramStructure.Companion.getNextToken
import xml_to_vm.ProgramStructure.Companion.checkNextToken
import xml_to_vm.ProgramStructure.Companion.contentTokenTrim
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
                            tempOutput += "add\n"
                            tempOutput += "pop pointer 1\n"
                            tempOutput += "push that 0\n"
                            output += tempOutput
                        } else {
                            output += "push ${typePopPush(type)} ${num}\n"
                        }
                    }
                }

                else -> {
                    throwNextToken() // <subroutineCall>
                    output += subroutineCall()

                }
            }

            throwNextToken()        // </term>
            return output
        }


        data class Result(val output:String , var listArgument:Int)
        fun expressionList() : Result {
            var listArg:Int = 0
            var output = ""
            if (!checkNextToken().contains("</expressionList>")) {

                do {
                    listArg++
                    if (checkNextToken().contains(","))
                        throwNextToken() // ,
                    throwNextToken() // <expression>
                    output += expression()
                } while (checkNextToken().contains(","))
            }
            throwNextToken() // </exprationList>

            return Result(output,listArg)
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

                        output += "push ${typePopPush(kind)} ${symbolTable.indexOf(name)}\n"
                        var (tmpOut,listArg) = expressionList()
                        throwNextToken() // )
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