package xml_to_vm

class SymbolTable {

    var classMap: HashMap<String, Triple<String, Kind, Int>> = hashMapOf()
    var functionMap : HashMap<String, Triple<String, Kind, Int>> = hashMapOf()
    var staticNum : Int = 0
    var fieldNum = 0
    var argNum = 0
    var varNum = 0
    var nameOfClass : String = ""
    var indexIf:Int = 0
    var indextWhile:Int = 0

    constructor(className : String){
        nameOfClass = className
        classMap = hashMapOf()
        staticNum = 0
        fieldNum = 0
        argNum = 0
        varNum = 0
        indexIf = 0
        indextWhile = 0
    }

    public fun getClassName() : String{
        return nameOfClass
    }

    public fun startSubroutine() {
        functionMap = hashMapOf()
        argNum = 0
        varNum = 0
        indexIf = 0
        indextWhile = 0
    }

    public fun defineClassMap(name: String, type : String, kind : Kind) {
        var k = when (kind){
            Kind.STATIC -> staticNum++
            Kind.FIELD -> fieldNum++
            else -> -1
        }
        classMap.set(name, Triple(type, kind, k))
    }

    public fun defineFunctionMap(name: String, type : String, kind : Kind) {
        var k =
        when (kind){
            Kind.ARG -> argNum++
            Kind.VAR -> varNum++
            else -> -1
        }
        functionMap.set(name, Triple(type, kind, k))
    }

    public fun varCount(kind : Kind) : Int{
        return when (kind){
            Kind.STATIC -> staticNum
            Kind.FIELD -> fieldNum
            Kind.ARG -> argNum
            Kind.VAR -> varNum
            else -> -1
        }
    }

    public fun kindOf(name : String) : Kind{
        var triple = functionMap.get(name)
        var x  = triple?.second
        if (x != null) {
               return x
        }
        triple = classMap.get(name)
        x = triple?.second
        if (x != null) {
            return x
        }
        return Kind.NONE
    }

    public fun indexOf(name : String) : Int{
        var triple = functionMap.get(name)
        var x  = triple?.third
        if (x != null) {
            return x
        }
        triple = classMap.get(name)
        x = triple?.third
        if (x != null) {
            return x
        }
        return -1
    }

    public fun typeOf (name : String) : String{
        var triple = functionMap.get(name)
        var x  = triple?.first
        if (x != null) {
            return x
        }
        triple = classMap.get(name)
        x = triple?.first
        if (x != null) {
            return x
        }
        return ""
    }

}