package argoko

sealed class Animal(val name : String) {
    abstract fun feed()
}

class Cat(name : String) : Animal(name) {
    override fun feed() {
        println("Whiskas")
    }
}

class Dog(name : String) : Animal(name) {
    override fun feed() {
        println("Pedigree chu")
    }
}

class Enclosure<T> {
    var t : T? = null

    fun moveInt(t : T) {
        this.t = t
    }


}