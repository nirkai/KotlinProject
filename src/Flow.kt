class Flow {

    companion object {
        var labelOfFunc = 0;

        fun label(fileLabel: String, label: String): String {
            return "(" + fileLabel + "." + label + ")\n"
        }

        fun goto(fileLabel: String, label: String): String {
            return "@" + fileLabel + "." + label + "\n" + "0;JMP\n"
        }

        fun ifGoto(fileLabel: String, label: String): String {
            return """
@SP
M=M-1
A=M
D=M
@""" + fileLabel + "." + label +
                    """
D;JNE
"""
        }


        fun call(f:String,num:Int,fileNameLable:String):String{
            var out = "@"
            // push return-address
            out+=f +""".ReturnAddress""" + labelOfFunc + """
D=A
@SP
A=M
M=D
@SP
M=M+1
"""
            var arr = arrayOf("LCL","ARG","THIS","THAT")
            for (argu in arr){
                out += tmpCall(argu)
            }
            // ARG = SP-n-5
            out +="""
@SP
D=M
@""" + (num + 5).toString() +"""
D=D-A
@ARG
M=D
"""
            // LCL=SP
            out += """
// LCL=SP
@SP
D=M
@LCL
M=D
"""
            // goto f
            out+= """
@""" + f + """
0;JMP
"""
            // label return-address
            out+= "(" + f + ".ReturnAddress" + labelOfFunc++ + ")\n"
            return out
        }

        private fun tmpCall(argu: String): String? {
            return """
@""" + argu + """
D=M
@SP
A=M
M=D
@SP
M=M+1
"""
        }

        fun function(f:String,k:String):String{
            return """
(""" + f + """)
@""" + k + """
D=A
@""" + f + """.End
D;JEQ
(""" + f +""".Loop)
@SP
A=M
M=0
@SP
M=M+1
@""" + f + """.Loop
D=D-1;JNE
(""" + f + """.End)
"""
        }
        fun returnFunc():String{
            var out = "\n//return func\n"
            // FRAME = LCL
            out+= """
@LCL
D=M
"""
            // RET = * (FRAME-5)
            // RAM[13] = (LOCAL - 5)
            out+= """
@5
A=D-A
D=M
@13
M=D
"""
            // * ARG = pop()
            out+= """
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
"""
            // SP = ARG+1
            out+= """
@ARG
D=M
@SP
M=D+1
"""
            var arr = arrayOf("THAT","THIS","ARG","LCL")
            for (argu in arr){
                out += tmpReturn(argu)
            }
            // goto RET
            out+= """
@13
A=M
0;JMP
"""
            return out
        }
        fun tmpReturn (argu:String):String{
            return  """
@LCL
M=M-1
A=M
D=M
@""" +argu + """
M=D
"""
        }
    }
}