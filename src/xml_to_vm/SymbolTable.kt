package xml_to_vm

class SymbolTable {

    var map: HashMap<String, Triple<String, String, Int>> = hashMapOf()

    constructor(){

    }

    public fun startSubroutine() {
        map = hashMapOf()
    }

    public fun define(name: String, type : String, kind : String) {

        map
    }


}