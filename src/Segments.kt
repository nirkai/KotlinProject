class Segments {
    companion object {

        fun constant(number: String) : String{
            var outPut = "\n// push constant\n" +
                    "@$number" +
                    "\n" +
                    "D=A\n" +
                    "@SP\n"+
                    "A=M\n"+
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n"
            return outPut
        }

        private fun pushTempPointerStatic(segment:String, number: String,fileNameLable: String): String {
            var num = "\n// push $segment $fileNameLable $number\n"
            when(segment){
                "temp"      -> num ="@${5 + number.toInt()}\n"
                "pointer"   ->  if (number.equals("0"))
                                    num += "@THIS\n"
                                else
                                    num += "@THAT\n"
                "static" -> {var i = 0
                    while(i <= number.toInt())
                        num += "@" + fileNameLable + "." + (i++) +"\n"
                }
            }
            return "" + num  +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n"
        }

        fun push(segment:String, number:String,fileNameLable:String):String{
            var output = "\n// push $segment $fileNameLable $number\n" +
                    "@$number\n" + "D=A\n"
            when(segment){
                "local"  -> output += "@LCL\n"
                "argument"          -> output += "@ARG\n"
                "this"              -> output += "@THIS\n"
                "that"              -> output += "@THAT\n"
                "constant"          -> return constant(number)
                "temp","pointer",
                    "static"        -> return pushTempPointerStatic(segment, number,fileNameLable)
            }
            output += "A=M+D\n" +
                    "D=M\n" +
                    "@SP\n" +
                    "A=M\n" +
                    "M=D\n" +
                    "@SP\n" +
                    "M=M+1\n"
            return output
        }

        fun pop(segment:String, number:String,fileNameLable:String):String{
            var output = "\n// pop $segment $fileNameLable $number\n" +
                    "@SP\nA=M-1\nD=M\n"
            when(segment){
                "local" -> output += "@LCL\n"
                "argument" -> output += "@ARG\n"
                "this" -> output += "@THIS\n"
                "that" -> output += "@THAT\n"
                "temp", "pointer","static" -> return popTempPointerStatic(segment, number,fileNameLable)
            }
            output += "A=M\n"
            var i = 0
            while (i++ < number.toInt())
                output += "A=A+1\n"
            output+= "M=D\n" +
                    "@SP\n" +
                    "M=M-1\n"
            return output
        }

        private fun popTempPointerStatic(segment:String, number: String, fileNameLable: String): String {
            var num = ""
            when(segment){
                "temp"      -> num += "@${5+number.toInt()}"
                "pointer"   ->  if (number.equals("0"))
                    num += "@THIS\n"
                else
                    num += "@THAT\n"
                "static" -> {var i = 0
                    while(i <= number.toInt())
                        num += "@$fileNameLable.${i++}\n"
                }
            }

            return """
                |// pop $segment $fileNameLable $number
                |@SP
                |A=M-1
                |D=M
                |$num
                |M=D
                |@SP
                |M=M-1
                |""".trimMargin()
        }

    }
}