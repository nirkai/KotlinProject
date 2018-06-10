package xml_to_vm

import xml_to_vm.Expressions.Companion.arrayTemp
import xml_to_vm.ProgramStructure.Companion.getNextToken
import xml_to_vm.ProgramStructure.Companion.checkNextToken
import xml_to_vm.ProgramStructure.Companion.contentTokenTrim
import xml_to_vm.ProgramStructure.Companion.throwNextToken
import xml_to_vm.ProgramStructure.*
import xml_to_vm.ProgramStructure.Companion.symbolTable
import xml_to_vm.SymbolTable
import xml_to_vm.Expressions.Companion.expression
import xml_to_vm.Expressions.Companion.subroutineCall
import xml_to_vm.ProgramStructure.Companion.typePopPush
class Statements {
    companion object {

        fun statements(): String {
            var output: String = ""
            output += statement()
            /*if (checkNextToken().contains("statement")) {
                throwNextToken() // <statement>
                output += statement()
            }*/
            throwNextToken() // </statements>
            return output
        }

        fun statement(): String {
            var output: String = ""
            var s = checkNextToken()
            while (s.contains("letStatement|ifStatement|whileStatement|doStatement|returnStatement".toRegex())) {
                throwNextToken() // <letStatement>|<ifStatement>|<whileStatement>|<doStatement>|<returnStatement>
                output += when {
                    s.contains("letStatement") -> letStatement()
                    s.contains("ifStatement") -> ifStatement()
                    s.contains("whileStatement") -> whileStatement()
                    s.contains("doStatement") -> {
                        doStatement()// + "pop temp 0\n"
                    }
                    s.contains("returnStatement") -> returnStatement()
                    else -> "error from function statement\n"
                }
                s = checkNextToken()
            }
                throwNextToken() // </statement>
            return output
        }

        private fun returnStatement(): String {
            var output =""
            throwNextToken() // return
            if (checkNextToken().contains(";")){ // void return
                output += "push constant 0\n"

            }
            else{
                throwNextToken() // <expression>
                output += expression()

            }
            output += "return\n"
            throwNextToken() // ;
            throwNextToken() // </returnStatement>
            return output
        }

        private fun doStatement(): String {
            var output = ""
            throwNextToken() // do
            if (checkNextToken().equals("<subroutineCall>"))
                throwNextToken() // subroutineCall
            output += subroutineCall()
            output += "pop temp 0\n"
            throwNextToken() // ;
            throwNextToken() // </doSubroutine>
            return output
        }

        private fun whileStatement(): String {
            var index = symbolTable.indexWhile
            symbolTable.indexWhile++
            var output = ""
            throwNextToken() // while
            throwNextToken() // (
            output += "label WHILE_EXP${index}\n"
            throwNextToken() // <expression>
            output += expression()
            throwNextToken() // )
            output += "not\n" +
                    "if-goto WHILE_END${index}\n"
            throwNextToken() // {
            throwNextToken() // <statements>
            output += statements()
            throwNextToken() // }
            output += "goto WHILE_EXP${index}\n" +
                    "label WHILE_END${index}\n"
            //symbolTable.indextWhile++
            return output
        }

        private fun ifStatement(): String {
            var index = symbolTable.indexIf
            symbolTable.indexIf++
            var output =""
            throwNextToken() // if
            throwNextToken() // (
            throwNextToken() // expression
            output += expression()
            throwNextToken() // )

            output += "if-goto IF_TRUE${index}\n"
            output += "goto IF_FALSE${index}\n"
            output += "label IF_TRUE${index}\n"
            throwNextToken() // {
            throwNextToken() // <statments>
            output += statements()
            throwNextToken() //}

            if (checkNextToken().contains("else")){
                output += "goto IF_END${index}\n" +
                        "label IF_FALSE${index}\n"
                throwNextToken() // else
                throwNextToken() // {
                throwNextToken() // <statements>
                output += statements()
                throwNextToken() // }
                output += "label IF_END${index}\n"
            }
            else
                output += "label IF_FALSE${index}\n"
            //symbolTable.indexIf++
            return output
        }

        private fun letStatement(): String {
            var output = ""

            throwNextToken() // let
            val name = contentTokenTrim(getNextToken())
            val type =  symbolTable.kindOf(name)
            val numOfVar = symbolTable.indexOf(name)
            if (contentTokenTrim(checkNextToken()).equals("[")) { // if let a[]  = kuku -- array
                throwNextToken() // [
                throwNextToken() // <expression>
                output += expression()
                //throwNextToken() // [
                throwNextToken() // ]
                output += "push ${typePopPush(type)} ${numOfVar}\n" +
                        "add\n"

                throwNextToken() // =
                throwNextToken() // <expression>
                output += expression()
                output += "pop temp 0\n" +
                        "pop pointer 1\n" +
                        "push temp 0\n" +
                        "pop ${typePopPush(type)} ${numOfVar}\n"
                //throwNextToken() // ]

            }

            else{ // if let a = kuku -- not array
                throwNextToken() // =
                throwNextToken() // <expression>
                output += expression()
                output +="pop ${typePopPush(type)} ${numOfVar}\n"

            }

            throwNextToken() // ;
            throwNextToken() // </letStatement>
            return output
        }

    }
}