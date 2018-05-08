class Flow {

    companion object {
        var labelOfFunc = 0;

        fun label(fileLabel: String, label: String): String {
            return "\n// label $fileLabel.$label\n" +
                    "($fileLabel.$label)\n"
        }

        fun goto(fileLabel: String, label: String): String {
            return "\n// goto $fileLabel .$label\n" +
                    "@$fileLabel.$label\n" + "0;JMP\n"
        }

        fun ifGoto(fileLabel: String, label: String): String {
            return """
                |// if-goto $fileLabel.$label
                |@SP
                |M=M-1
                |A=M
                |D=M
                |@$fileLabel.$label
                |D;JNE
                |""".trimMargin()
        }


        fun call(f:String,num:Int,fileNameLable:String):String{
            var out = """
                |// call $f.ReturnAddress$num
                |@$f.ReturnAddress$labelOfFunc
                |D=A
                |@SP
                |A=M
                |M=D
                |@SP
                |M=M+1
                |""".trimMargin()
            var arr = arrayOf("LCL","ARG","THIS","THAT")
            for (argu in arr){
                out += tmpCall(argu)
            }

            out +="""
                |// ARG = SP-n-5
                |@SP
                |D=M
                |@${num + 5}
                |D=D-A
                |@ARG
                |M=D
                |""".trimMargin()
            // LCL=SP
            out += """
                |// LCL=SP
                |@SP
                |D=M
                |@LCL
                |M=D
                |""".trimMargin()
            // goto f
            out+= """
                |// goto f
                |@$f
                |0;JMP
                |""".trimMargin()
            // label return-address
            out+= "($f.ReturnAddress${labelOfFunc++})\n"
            return out
        }

        private fun tmpCall(argu: String): String? {
            return """
                |@$argu
                |D=M
                |@SP
                |A=M
                |M=D
                |@SP
                |M=M+1
                |""".trimMargin()
        }

        fun function(f:String,k:String):String{
            return """
                |// function $f $k
                |($f)
                |@$k
                |D=A
                |@$f.End
                |D;JEQ
                |($f.Loop)
                |@SP
                |A=M
                |M=0
                |@SP
                |M=M+1
                |@$f.Loop
                |D=D-1;JNE
                |($f.End)
                |""".trimMargin()
        }

        fun returnFunc():String{
            var out = """
                |   //return func
                |   // FRAME = LCL
                |@LCL
                |D=M
                |   // RET = * (FRAME-5)
                |   // RAM[13] = (LOCAL - 5)
                |@5
                |A=D-A
                |D=M
                |@13
                |M=D
                |   // * ARG = pop()
                |@SP
                |M=M-1
                |A=M
                |D=M
                |@ARG
                |A=M
                |M=D
                |   // SP = ARG+1
                |@ARG
                |D=M
                |@SP
                |M=D+1
                |""".trimMargin()

            var arr = arrayOf("THAT","THIS","ARG","LCL")
            for (argu in arr){
                out += tmpReturn(argu)
            }
            // goto RET
            out+= """
                |@13
                |A=M
                |0;JMP
                |""".trimMargin()
            return out
        }

        fun tmpReturn (argu:String):String{
            return  """
                |@LCL
                |M=M-1
                |A=M
                |D=M
                |@$argu
                |M=D
                |""".trimMargin()
        }
    }
}