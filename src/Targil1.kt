import java.awt.font.NumericShaper
import java.io.File
import java.io.FileWriter

var lableNum = 0

fun main(args: Array<String>) {
    // var p = "C:\\Users\\ניר\\Documents\\מדעי המחשב\\שנה ג - תשעח\\סימסטר ב\\עקרונות שפות תכנה\\Exercises\\Targil2\\project 08\\FunctionCalls\\StaticsTest"
    var filePath = System.getProperty("user.dir")
    //val filePath = p

    if (args.size > 0){
        println(args.get(0))

        filePath = args.get(0)
        if (filePath.isNullOrEmpty()) {
            println("problem, file is: " + filePath)
            filePath = System.getProperty("user.dir")
        }
    }
    val title =    """

//  ***********************************************
//  *            --- Created By ---               *
//  *     David Grofman                           *
//  * &&                                          *
//  *     Nir Kaizler                             *
//  *                                             *
//  ***********************************************

                """.trimIndent()

    val sys = File(filePath + "\\Sys.vm")
    if (sys.isFile){
        var outputFileName = "output"
        File(filePath).walk().forEach { tstFile ->
            if (tstFile.isFile && tstFile.name.contains(".tst") && !tstFile.name.contains("VME.tst")){
                outputFileName = tstFile.name
            }
        }
        outputFileName = outputFileName.removeSuffix(".tst")
        val outStream = FileWriter(filePath + "\\" +outputFileName + ".asm")

        outStream
        outStream.write("// \t\t\t---- $outputFileName ASM file ----\n\n$title" +
                "\n// restart the stack\n" +
                "@256\n" +
                "D=A\n" +
                "@0\n" +
                "M=D\n" +
                Flow.call("Sys.init", 0, "Sys"))
        println("Print sys.vm file!!!")
        var output = ""
        //outStream.append(output)
        //val path = System.getProperty("user.dir")
        println("\nWorking Directory = $filePath \n")
        File(filePath).walk().forEach { fileVm ->
            if (fileVm.isFile && fileVm.name.contains(".vm")){
                output = vmToAsm(fileVm)
                outStream.append(output)
            }
        }
        outStream.close()
    }
    else{
        File(filePath).walk().forEach { fileVm ->
            var asmfile = fileVm.name.removeSuffix(".vm")
            if (fileVm.isFile && fileVm.name.contains(".vm")){
                var outStream = FileWriter(filePath + "\\" + asmfile + ".asm")
                outStream.write("// \t\t\t---- $asmfile ASM file ----\n\n$title" +
                        "\n// restart the stack\n" +
                        "@256\n" +
                        "D=A\n" +
                        "@0\n" +
                        "M=D\n")
                var output = vmToAsm(fileVm)
                outStream.append(output)
                outStream.close()
            }
        }
    }


    /*File("vm").walk().forEach { fileVm ->
        var str = fileVm.toString()
        if (fileVm.isFile) {
            println("absolute file: " + fileVm.absoluteFile)
            println("absolute path: " + fileVm.absolutePath)
            println("name: " + fileVm.name)
        }



        if (str.contains(".vm") == true) {
            val inputStream = File(str)
            var fileNameLable = fileVm.name
            vmToAsm(fileVm)
            if (fileVm.name == "Sys.vm")
            //fileNameLable = fileNameLable.removePrefix(fileVm.name)
            str = str.removeSuffix(".vm")
            str += ".asm"
            // val inputStream  = File("vm\\BasicTest.vm")
            val lineList = mutableListOf<String>()
            var hackProgramString = "@256\nD=A\n@0\nM=D\n"
            val outStream = File(str)
            // val outStream = File("vm\\BasicTest.asm")
            var lableNum = 0
            inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
            lineList.forEach {
                it.toLowerCase()
                var y: List<String> = it.split(" ")
                var x = y.get(0)
                y = y.subList(1, y.size)
                when (x) {
                    "\\" -> ""
                    "label" -> hackProgramString += Flow.label(fileNameLable, y[0])
                    "goto" -> hackProgramString += Flow.goto(fileNameLable, y[0])
                    "if-goto" -> hackProgramString += Flow.ifGoto(fileNameLable, y[0])
                    "call" -> hackProgramString += Flow.call(y[0],y[1].toInt(),fileNameLable)
                    "function" -> hackProgramString += Flow.function(y[0],y[1])
                    "return" -> hackProgramString += Flow.returnFunc()
                    "push" -> hackProgramString += Segments.push(y[0], y[1],fileNameLable)
                    "pop" -> hackProgramString += Segments.pop(y[0], y[1],fileNameLable)
                    "add" -> hackProgramString += Arithmetics.add()
                    "sub" -> hackProgramString += Arithmetics.sub()
                    "neg" -> hackProgramString += Arithmetics.neg()
                    "eq" -> hackProgramString += Arithmetics.eq(lableNum++)
                    "gt" -> hackProgramString += Arithmetics.gt(lableNum++)
                    "lt" -> hackProgramString += Arithmetics.lt(lableNum++)
                    "and" -> hackProgramString += Arithmetics.and()
                    "or" -> hackProgramString += Arithmetics.or()
                    "not" -> hackProgramString += Arithmetics.not()
                }
            }
            outStream.writeText(hackProgramString)

        }
    }*/
}

fun vmToAsm(fileVm: File) : String{
    println(fileVm.absolutePath + " " + fileVm.name)
    var str = fileVm.absolutePath
    val inputStream = File(str)
    var fileNameLable = fileVm.name.removeSuffix(".vm")
    //str = str.removeSuffix(".vm")


    val lineList = mutableListOf<String>()
    var hackProgramString = ""


    inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
    lineList.forEach {
        it.toLowerCase()
        var y: List<String> = it.split(" ", "\t")
        var x = y.get(0)
        y = y.subList(1, y.size)
        when (x) {
            "\\" -> ""
            "label" -> hackProgramString += Flow.label(fileNameLable, y[0])
            "goto" -> hackProgramString += Flow.goto(fileNameLable, y[0])
            "if-goto" -> hackProgramString += Flow.ifGoto(fileNameLable, y[0])
            "call" -> hackProgramString += Flow.call(y[0],y[1].toInt(),fileNameLable)
            "function" -> hackProgramString += Flow.function(y[0],y[1])
            "return" -> hackProgramString += Flow.returnFunc()
            "push" -> hackProgramString += Segments.push(y[0], y[1],fileNameLable)
            "pop" -> hackProgramString += Segments.pop(y[0], y[1],fileNameLable)
            "add" -> hackProgramString += Arithmetics.add()
            "sub" -> hackProgramString += Arithmetics.sub()
            "neg" -> hackProgramString += Arithmetics.neg()
            "eq" -> hackProgramString += Arithmetics.eq(lableNum++)
            "gt" -> hackProgramString += Arithmetics.gt(lableNum++)
            "lt" -> hackProgramString += Arithmetics.lt(lableNum++)
            "and" -> hackProgramString += Arithmetics.and()
            "or" -> hackProgramString += Arithmetics.or()
            "not" -> hackProgramString += Arithmetics.not()
        }
    }
    return hackProgramString
}

