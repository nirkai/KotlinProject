class Arithmetics {
    companion object {
        fun add(): String {
            return "\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=D+M\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun sub(): String {
            return "\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=M-D\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun neg(): String {
            return "\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "@0\n" +
                    "D=A-D\n" +
                    "A=M-1\n" +
                    "M=D\n"

        }

        fun eq(index: Int): String {
            return "\n@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "D=D-M\n" +
                    "@IF_TRUE" + index + "\n" +
                    "D;JEQ\n" +
                    "D=0\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "@IF_FALSE" + index + "\n" +
                    "0;JMP\n" +
                    "(IF_TRUE" + index + ")\n" +
                    "D=-1\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "(IF_FALSE" + index + ")\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun gt(index: Int): String {
            return "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "D=M-D\n" +
                    "@IF_TRUE" + index + "\n" +
                    "D;JGT\n" +
                    "D=0\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "@IF_FALSE" + index + "\n" +
                    "0;JMP\n" +
                    "(IF_TRUE" + index + ")\n" +
                    "D=-1\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "(IF_FALSE" + index + ")\n" +
                    "@SP\n" +
                    "M=M-1"
        }

        fun lt(index: Int): String {
            return "\n@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "D=M-D\n" +
                    "@IF_TRUE" + index + "\n" +
                    "D;JLT\n" +
                    "D=0\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "@IF_FALSE" + index + "\n" +
                    "0;JMP\n" +
                    "(IF_TRUE" + index + ")\n" +
                    "D=-1\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "(IF_FALSE" + index + ")\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun and(): String{
            return """
@SP
A=M
A=A-1
D=M
A=A-1
M=M&D
@SP
M=M-1
"""
        }

        fun or(): String{
            return """
@SP
A=M
A=A-1
D=M
A=A-1
M=M|D
@SP
M=M-1
"""
        }

        fun not(): String {
            return """
@SP
A=M-1
D=-1
D=D-M
M=D
"""
        }

       /* fun push(arg1: String, arg2: String): String {
            var outPut = ""
            *//*if (arg2.toDouble() >= 0)
        outPut = "\n@" + arg2 +"\n" + "D=A\n" + "@0\nA=M\nM=D\n"
    else*//*
            //outPut = "\n" + "D=" + arg2 + "\n@0\nA=M\nM=D\n"
            outPut = "\n@" + arg2 + "\n" + "D=A\n" + "@0\nA=M\nM=D\n"
            outPut += moveSpStep()

            return outPut
        }

        fun moveSpStep(): String {
            return "\n" +
                    "D=A\n" +
                    "D=D+1\n" +
                    "@0\n" +
                    "M=D"
        }*/
    }
}
