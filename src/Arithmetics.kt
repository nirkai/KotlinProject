class Arithmetics {
    companion object {
        fun add(): String {
            return "\n// add\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=D+M\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun sub(): String {
            return "\n// sub\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=M-D\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun neg(): String {
            return "\n// negative\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "@0\n" +
                    "D=A-D\n" +
                    "A=M-1\n" +
                    "M=D\n"

        }

        fun eq(index: Int): String {
            return "\n// eq - equal, with label number $index\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "D=D-M\n" +
                    "@IF_TRUE$index\n" +
                    "D;JEQ\n" +
                    "D=0\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "@IF_FALSE$index\n" +
                    "0;JMP\n" +
                    "(IF_TRUE$index)\n" +
                    "D=-1\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "(IF_FALSE$index)\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun gt(index: Int): String {
            return "\n// gt - grater then, with label number $index\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "D=M-D\n" +
                    "@IF_TRUE$index\n" +
                    "D;JGT\n" +
                    "D=0\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "@IF_FALSE$index\n" +
                    "0;JMP\n" +
                    "(IF_TRUE$index)\n" +
                    "D=-1\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "(IF_FALSE$index)\n" +
                    "@SP\n" +
                    "M=M-1"
        }

        fun lt(index: Int): String {
            return "\n// lt - less then, with label number $index\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "D=M-D\n" +
                    "@IF_TRUE$index\n" +
                    "D;JLT\n" +
                    "D=0\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "@IF_FALSE$index\n" +
                    "0;JMP\n" +
                    "(IF_TRUE$index)\n" +
                    "D=-1\n" +
                    "@SP\n" +
                    "A=M-1\n" +
                    "A=A-1\n" +
                    "M=D\n" +
                    "(IF_FALSE$index)\n" +
                    "@SP\n" +
                    "M=M-1\n"
        }

        fun and(): String{
            return "\n// and\n" + """
            |@SP
            |A=M
            |A=A-1
            |D=M
            |A=A-1
            |M=M&D
            |@SP
            |M=M-1
            |""".trimMargin()
        }

        fun or(): String{
            return "\n// or\n" + """
            |@SP
            |A=M
            |A=A-1
            |D=M
            |A=A-1
            |M=M|D
            |@SP
            |M=M-1
            |""".trimMargin()
        }

        fun not(): String {
            return "\n// ~ not\n" + """
            |@SP
            |A=M-1
            |D=-1
            |D=D-M
            |M=D
            |""".trimMargin()
        }
    }
}
