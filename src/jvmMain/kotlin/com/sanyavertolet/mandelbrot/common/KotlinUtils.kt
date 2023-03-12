/**
 * Utils for kotlin lang
 */

package com.sanyavertolet.mandelbrot.common

/**
 * @param defaultValue default value that should be set if [this] is null
 * @return [this] or [defaultValue] if [this] is null
 */
fun <T : Any> T?.or(defaultValue: T) = this ?: defaultValue

/**
 * @param defaultValueProducer lazy default value producer that should be set if [this] is null
 * @return [this] or value produced by [defaultValueProducer] if [this] is null
 */
fun <T : Any> T?.or(defaultValueProducer: () -> T) = this ?: defaultValueProducer()
