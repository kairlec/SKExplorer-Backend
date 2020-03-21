package com.kairlec.utils

import kotlin.random.Random

/**
 * @program: SKExplorer
 * @description: 字符串拓展
 * @author: Kairlec
 * @create: 2020-03-17 13:42
 */
fun ClosedRange<Char>.randomString(length: Int) = (1..length).map { (Random.nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }.joinToString("")

fun String.Companion.random(range: ClosedRange<Char>, length: Int) = range.randomString(length)
