package br.com.rafaelportela.demo

class Article() {
    lateinit var id: String
    lateinit var title: String
    var likes: Int? = null

    constructor(id: String, title: String) : this() {
        this.id = id
        this.title = title
    }

    constructor(id: String, title: String, likes: Int) : this() {
        this.id = id
        this.title = title
        this.likes = likes
    }
}