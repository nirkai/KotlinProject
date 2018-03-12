import com.sun.javafx.animation.TickCalculation.sub
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val inputStream  = File("vm\\SimpleSub.vm")
    val lineList = mutableListOf<String>()
    var hackProgramString = "@256\nD=A\n@0\nM=D"
    val outStream = File("vm\\SimpleSub.asm")

    inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it)} }
    lineList.forEach{
        var y: List<String> = it.split(" ")
        var x = y.get(0)
        y= y.subList(1,y.size)
        when(x){
            "\\" -> ""
            "push" -> hackProgramString+=push(y[0],y[1])
            "add" -> hackProgramString+=add()
            "sub" -> hackProgramString+=sub()
        }

    }
    outStream.writeText(hackProgramString)
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

fun push(arg1: String, arg2:String):String{
    var outPut = "\n@" + arg2 +"\n" + "D=A\n" + "@0\nA=M\nM=D\n"
    outPut+=moveSpStep()

    return outPut
}

fun moveSpStep():String {
    return "\n" +
            "D=A\n" +
            "D=D+1\n" +
            "@0\n" +
            "M=D"
}

/*fun add():String {
    return "\n@0\nA=M\nA=A-1\nA=A-1\nD=M\nA=A+1\nA=M\nD=D+A\n@0\nA=M\nA=A-1\nA=A-1\nM=D\n@0\nD=M\nD=D-1\nM=D\n"
}*/
/*
fun add():String {
    return "\n@0\nA=M\nA=A-1\nA=A-1\nD=M\nA=A+1\nA=M\nD=D+A\n@0\nA=M\nA=A-1\nA=A-1\nM=D\n@0\nM=M-1\n"
}*/
fun add():String {
    return "\n" +
            "@SP\n" +
            "A=M-1\n" +
            "D=M\n" +
            "A=A-1\n" +
            "M=D+M\n" +
            "@SP\n" +
            "M=M-1\n"
}