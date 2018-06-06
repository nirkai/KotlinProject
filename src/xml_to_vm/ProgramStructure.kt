package xml_to_vm

class ProgramStructure {
    companion object {
        var tab: Int = 0
        @JvmStatic
        var inputFile = ""

        fun classVM() : String{
            return ""
        }

        fun getNextToken() : String {

            var s = inputFile.substring(0, inputFile.indexOf("\n")+1)
            var g = inputFile.substring(inputFile.indexOf("\n")+1)
            inputFile = g
            return s.trimStart(' ')
        }

        fun throwNextToken() : String{
            getNextToken()
            return ""
        }

        fun checkNextToken() : String {
            return inputFile.substring(0, inputFile.indexOf("\n")).trimStart(' ')
        }

        fun checkFollow1Token() : String {
            var dump = inputFile.substring(inputFile.indexOf("\n") + 1)
            return dump.substring(0, inputFile.indexOf("\n")).trimStart(' ')
        }
    }
}