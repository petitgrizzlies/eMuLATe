
import machine.learning.ARL
import java.io.File
import kotlin.collections.ArrayList

fun main(args : Array<String>) {

//    // we open the .dat file located in data
//    val myFile = File("data/EURUSD.dat").inputStream()
//    val array2: ArrayList<Double> = arrayListOf()
//
//    myFile.bufferedReader().useLines { lines ->
//        lines.forEach {
//            array2.add(it.split("/")[1].split(" ")[0].toDouble())
//        }
//    }
    val myFile = File("data/EURUSD_2000_2001.csv").inputStream()
    val array2 : ArrayList<Double> = arrayListOf()
    myFile.bufferedReader().useLines {
        lines -> lines.forEach {
            array2.add(it.split(" ")[1].split(";")[1].toDouble())
        }
    }

    val time = System.currentTimeMillis()
    val arl = ARL(20)

    var i = 0
    var p_t = arrayOf(1.0)
    val step = 2000
    val stepLearn = 2500
    val n = 50000
    val updateThreshold = 200

    while(i < n) {
        println("$i")
        arl.train(array2.toDoubleArray().slice(i..i+step), updateThreshold, p_t)
        p_t = arl.test(array2.toDoubleArray().slice(i+step..i+stepLearn), p_t)
        arl.reset()
        i += stepLearn - step
    }
    arl.saveInFile()

    println("time = ${(System.currentTimeMillis() - time) / 1000}")

}
