package com.sanyavertolet.mandelbrot.complex

import kotlin.math.sqrt
import kotlin.random.Random

class Complex(val re: Double = 0.0, val im: Double = 0.0): Comparable<Complex> {
    private fun sqr() = re * re + im * im

    fun abs() = sqrt(sqr())

    operator fun not() = Complex(re, -im)

    operator fun times(other: Double) = Complex(
        re * other,
        im * other,
    )

    operator fun div(other: Double) = if (other != 0.0) {
        Complex(
            re / other,
            im / other,
        )
    } else {
        throw ArithmeticException("Division by zero")
    }

    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)

    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)

    operator fun times(other: Complex) = Complex(
        re * other.re - im * other.im,
        re * other.im + im * other.re,
    )

    operator fun div(other: Complex) = this * !other / other.sqr()

    override fun compareTo(other: Complex) = abs().compareTo(other.abs())

    override fun toString() = "($re, $im)"
}

fun Random.nextComplex(maxDouble: Double = MAX_DOUBLE) = Complex(nextDouble(maxDouble), nextDouble(maxDouble))

private const val MAX_DOUBLE = 1000000.0