package xml_to_vm

import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {

    var filePath = System.getProperty("user.dir")

    if (args.size > 0){
        println(args.get(0))
        if (Files.isDirectory(Paths.get(args.get(0)))) {
            filePath = args.get(0)
        }
        else {
            println("Invalid path!\n")
            filePath = System.getProperty("user.dir")
            return
        }
    }


}