package com.sanyavertolet.mandelbrot.complex

import kotlin.math.sqrt
import kotlin.random.Random

private const val MAX_DOUBLE = 1_000_000.0

/**
 * @property re real part of complex number
 * @property im imaginary part of complex number
 */
class Complex(val re: Double = 0.0, val im: Double = 0.0) : Comparable<Complex> {
    private fun sqr() = re * re + im * im

    /**
     * @return complex vector length
     */
    fun abs() = sqrt(sqr())

    /**
     * @return [Complex] with negative [im]
     */
    operator fun not() = Complex(re, -im)

    /**
     * @param other rational number
     * @return this [Complex] multiplied by [other]
     */
    operator fun times(other: Double) = Complex(
        re * other,
        im * other,
    )

    /**
     * @param other rational number
     * @return this [Complex] divided by [other]
     * @throws ArithmeticException on null division
     */
    operator fun div(other: Double) = if (other != 0.0) {
        Complex(
            re / other,
            im / other,
        )
    } else {
        throw ArithmeticException("Division by zero")
    }

    /**
     * @param other
     * @return this [Complex] increased by [other]
     */
    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)

    /**
     * @param other
     * @return this [Complex] decreased by [other]
     */
    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)

    /**
     * @param other
     * @return this [Complex] multiplied by [other]
     */
    operator fun times(other: Complex) = Complex(
        re * other.re - im * other.im,
        re * other.im + im * other.re,
    )

    /**
     * @param other
     * @return this [Complex] divided by [other]
     */
    operator fun div(other: Complex) = this * !other / other.sqr()

    override fun compareTo(other: Complex) = abs().compareTo(other.abs())

    override fun toString() = "($re, $im)"
}

/**
 * @param maxDouble
 * @return random [Complex] number
 */
fun Random.nextComplex(maxDouble: Double = MAX_DOUBLE) = Complex(nextDouble(maxDouble), nextDouble(maxDouble))
