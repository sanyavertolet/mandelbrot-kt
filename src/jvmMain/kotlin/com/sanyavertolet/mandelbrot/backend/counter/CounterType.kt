package com.sanyavertolet.mandelbrot.backend.counter

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.Painter

typealias CounterBuilder = (Function, Painter, Int, Double) -> Counter

/**
 * @property prettyName
 * @property getInstance
 */
enum class CounterType(val prettyName: String, val getInstance: CounterBuilder) {
    PARALLEL("Parallel", { function,
        painter,
        maxIterations,
        borderValue ->
        ParallelCounter(painter, function, maxIterations, borderValue)
    }),
    SERIAL("Serial", { function,
        painter,
        maxIterations,
        borderValue ->
        SerialCounter(painter, function, maxIterations, borderValue)
    }),
    ;
}
