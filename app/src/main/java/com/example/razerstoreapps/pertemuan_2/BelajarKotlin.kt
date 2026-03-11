package com.example.razerstoreapps.pertemuan_2

fun main() {
    println("Hai rekan_rekan...")
    println("selamat datang di bahasa pemrograman kotlin")

    println("=========")

    var angka = 15
    println("hasil dari 15 + 10 = ${angka + 10}")

    val nilaiInt = 10000
    val nilaiDouble = 100.003
    val nilaiFloat = 1000.0f

    println("======STRING======")
    val huruf = 'a'
    println("ini penggunaan karacter '$huruf'")

    val nilaiString = "mawar"
    println("halo $nilaiString!\nApa kabar?")

    println("========kondisi=========")

    val nilai = 10
    if (nilai < 0)
        println("bilangan negatif")
    else {
        if (nilai % 2 == 0)
            println("bilangan genap")
        else
            println("bilangan ganil")
    }

    println("==========perulangan=========")
    val KampusKu: Array<String> = arrayOf("kampus", "politeknik", "caltex", "riau")
    for (kampus: String in KampusKu) {
        println(kampus)
    }

}

