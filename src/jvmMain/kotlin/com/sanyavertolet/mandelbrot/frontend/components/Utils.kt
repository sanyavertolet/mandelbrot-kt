/**
 * File that contains different frontend utils and type aliases
 */

package com.sanyavertolet.mandelbrot.frontend.components

/**
 * Type alias for a lambda that receives update callback
 *
 * Should be equal to something like this:
 *
 * __{ valueT = it(valueT) }__
 */
typealias StateUpdater<T> = ((T) -> T) -> Unit
