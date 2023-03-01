/**
 * Function classes
 */

@file:Suppress("WRONG_WHITESPACE")

package com.sanyavertolet.mandelbrot.backend

import com.sanyavertolet.mandelbrot.backend.complex.Complex

/**
 * Class that implements fractal dot-wise calculation
 */
sealed class Function {
    /**
     * Mandelbrot and Julia both have the same function: z_{n+1} = z_n^2 + c
     *
     * @param z current point
     * @param c previously calculated number
     * @return calculated fractal function
     */
    @Suppress("IDENTIFIER_LENGTH")
    operator fun invoke(z: Complex, c: Complex): Complex = z * z + c

    /**
     * @param currentPoint current point of complex plane
     * @return z_0
     * @see [invoke]
     */
    abstract fun getStartingValue(currentPoint: Complex): Complex

    /**
     * @param currentPoint current point of complex plane
     * @return c
     * @see [invoke]
     */
    abstract fun getConstant(currentPoint: Complex): Complex
}

/**
 * @property prettyName human-readable name of Function
 * @property instance instance of [Function] class
 */
enum class FunctionType(val prettyName: String, val instance: Function) {
    JULIA("Julia", JuliaFunction) ,
    MANDELBROT("Mandelbrot", MandelbrotFunction),
    ;
}

object JuliaFunction : Function() {
    var constant: Complex = Complex()
    override fun getConstant(currentPoint: Complex): Complex = constant

    override fun getStartingValue(currentPoint: Complex): Complex = currentPoint
}

object MandelbrotFunction : Function() {
    override fun getConstant(currentPoint: Complex): Complex = currentPoint

    override fun getStartingValue(currentPoint: Complex): Complex = Complex()
}
