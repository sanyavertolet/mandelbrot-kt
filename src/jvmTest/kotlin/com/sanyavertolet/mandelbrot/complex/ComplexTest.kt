package com.sanyavertolet.mandelbrot.complex

import com.sanyavertolet.mandelbrot.backend.complex.nextComplex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import kotlin.math.sqrt
import kotlin.random.Random

class ComplexTest {
    @RepeatedTest(REPEAT_TIMES)
    fun abs() {
        val complex = Random.nextComplex()
        assertEquals(complex.abs(), sqrt(complex.re * complex.re + complex.im * complex.im))
    }

    @RepeatedTest(REPEAT_TIMES)
    fun not() {
        val complex = Random.nextComplex()
        assertEquals(complex.not().re, complex.re)
        assertEquals(complex.not().im, -complex.im)
    }

    @RepeatedTest(REPEAT_TIMES)
    fun timesDouble() {
        val complex = Random.nextComplex()
        val double = Random.nextDouble()
        assertEquals(complex.times(double).re, complex.re * double)
        assertEquals(complex.times(double).im, complex.im * double)
    }

    @RepeatedTest(REPEAT_TIMES)
    fun divDouble() {
        val complex = Random.nextComplex()
        val double = Random.nextDouble()
        assertEquals(complex.div(double).re, complex.re / double)
        assertEquals(complex.div(double).im, complex.im / double)
    }

    @RepeatedTest(REPEAT_TIMES)
    fun plus() {
        val lhs = Random.nextComplex()
        val rhs = Random.nextComplex()
        assertEquals(lhs.plus(rhs).re, lhs.re + rhs.re)
        assertEquals(lhs.plus(rhs).im, lhs.im + rhs.im)
    }

    @RepeatedTest(REPEAT_TIMES)
    fun minus() {
        val lhs = Random.nextComplex()
        val rhs = Random.nextComplex()
        assertEquals(lhs.minus(rhs).re, lhs.re - rhs.re)
        assertEquals(lhs.minus(rhs).im, lhs.im - rhs.im)
    }

    @RepeatedTest(REPEAT_TIMES)
    fun times() {
        val lhs = Random.nextComplex()
        val rhs = Random.nextComplex()
        assertEquals(lhs.times(rhs).re, lhs.re * rhs.re - lhs.im * rhs.im)
        assertEquals(lhs.times(rhs).im, lhs.re * rhs.im + lhs.im * rhs.re)
    }

    @RepeatedTest(REPEAT_TIMES)
    fun div() {
        val lhs = Random.nextComplex()
        val rhs = Random.nextComplex()
        assertEquals(lhs.times(rhs).re, lhs.re * rhs.re - lhs.im * rhs.im)
        assertEquals(lhs.times(rhs).im, lhs.re * rhs.im + lhs.im * rhs.re)
    }

    companion object {
        private const val REPEAT_TIMES = 10
    }
}