package machine.learning

import java.util.*
import kotlin.collections.ArrayList
import computeFt

/**
 *  It's the main classe for the Adaptive reinforcement learning
 *
 *  @param arrayPrices the prices data the algorithm will work with. The type is arrayList to get compatibility
 *  with Java.
 *  @param vThreshold the threshold of the neural net
 *  @param sizeWindow the window size of the element to watch during computation
 *
 *  @author Romain Mencattini
 */
class ARL(private val arrayPrices: ArrayList<Double>, private val vThreshold: Double, private val sizeWindow: Int) {

    private var z: Double
    private var weight : Weights
    private var parameters : Parameters
    private var ft: Array<Pair<Double, Double>> // where first = the sign, second = the value
    private var returns: DoubleArray
    private var prices: DoubleArray

    init {

        val random = Random()

        prices = arrayPrices.toDoubleArray()

        parameters = Parameters()
        z = random.nextDouble()

        // create an array of weight with size of $sizeWindow
        weight = Weights(sizeWindow, 0)
        returns = DoubleArray(0)

        // the old value
        ft = arrayOf(Pair(0.0, 0.0))

    }

    /**
     * It will reset the returns and the prices.
     * It's useful between two runs to keep the weights and the parameters but not the rest.
     */
    fun reset() {
        prices = DoubleArray(0)
        returns = DoubleArray(0)
    }

    /**
     * It sets the prices for a new runs
     */
    fun setPrices(prices: DoubleArray) {
        this.prices = prices
    }

    /**
     * In the function we will improve the weights and parameters. This is the learning phase.
     */
    fun trainingLoop(givenT: Int = 1) {

        var t = givenT
        var oldPrice = prices[t - 1]
        // the training is done over every prices. From the soonest to the latest.
        for (price in prices.sliceArray(t..(prices.size - 1))) {

            // compute the return
            val computedReturn = price - oldPrice
            // keep the price for the next loop
            oldPrice = price
            // store the computedReturn in returns
            returns = returns.plus(computedReturn)

            // compute the Ft
            ft = ft.plus(computeFt(givenT))

            // update the weights
            weight = weight.updateWeights(givenT, parameters, ft, returns)

            // if the numbers of steps is reach, update the parameters i.e : delta, rho, ...

            // increase the givenT size
            t++
        }
    }


    /**
     * Compute the Ft layer using weights, vthreshold, returns and old_ft.
     * Given t, my need are :
     * - compute ft
     * - return the value and the sign
     *
     * @param givenT an Int. It's our index.
     * @return a pair of signum and value
     */
    private fun computeFt(givenT : Int) : Pair<Double, Double> {

        return computeFt(givenT, weight, ft, sizeWindow, returns, parameters)
    }

    override fun toString(): String {
        return "ARL(" +
                "parameters=$parameters,\n" +
                "ft=${Arrays.toString(ft)},\n" +
                "returns=${Arrays.toString(returns)}\n," +
                "weigths=" + weight.toString() + ")"
    }


}