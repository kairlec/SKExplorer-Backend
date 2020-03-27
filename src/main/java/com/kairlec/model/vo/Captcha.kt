package com.kairlec.model.vo

import com.kairlec.model.bo.SKImage
import java.awt.Color
import java.awt.Font
import java.awt.FontFormatException
import java.awt.image.BufferedImage
import java.io.IOException
import java.util.*

class Captcha private constructor(val captchaString: String, val skImage: SKImage) {

    override fun toString(): String {
        return captchaString
    }

    fun check(captchaString: String, ignoreCase: Boolean = true): Boolean {
        return captchaString.equals(this.captchaString, ignoreCase)
    }

    companion object {
        private val mapTable = "abcdefghijkmnopqrstvwxyzABCDEFGHJKLMNOPQRSTVWXYZ123456789".toCharArray()

        //给定范围获得随机颜色
        private fun getRandColor(_fc: Int, _bc: Int): Color {
            val fc = if (_fc > 255) 255 else _fc
            val bc = if (_bc > 255) 255 else _bc
            val random = Random()
            val r = fc + random.nextInt(bc - fc)
            val g = fc + random.nextInt(bc - fc)
            val b = fc + random.nextInt(bc - fc)
            return Color(r, g, b)
        }

        fun getInstant(captchaCount: Int): Captcha {
            val width = 80
            val height = 30
            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
            // 获取图形上下文
            val g = image.graphics
            //生成随机类
            val random = Random()
            // 设定背景色
            g.color = getRandColor(200, 250)
            g.fillRect(0, 0, width, height)
            //设定字体
            try {
                val font = Font.createFont(Font.TRUETYPE_FONT, Captcha::class.java.getResourceAsStream("/fonts/ComingSoon-Regular.ttf"))
                g.font = font.deriveFont(Font.BOLD, 19f)
            } catch (e: FontFormatException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            // 随机产生168条干扰线，使图象中的认证码不易被其它程序探测到
            g.color = getRandColor(160, 200)
            for (i in 0..167) {
                val x = random.nextInt(width)
                val y = random.nextInt(height)
                val xl = random.nextInt(12)
                val yl = random.nextInt(12)
                g.drawLine(x, y, x + xl, y + yl)
            }
            //取随机产生的码
            val strEnsure = StringBuilder()
            for (i in 0 until captchaCount) {
                strEnsure.append(mapTable[(mapTable.size * Math.random()).toInt()])
                // 将认证码显示到图象中
                g.color = Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110))
                // 直接生成
                val str = strEnsure.substring(i, i + 1)
                // 设置随便码在背景图图片上的位置
                g.drawString(str, 16 * i + 10 + random.nextInt(3), 15 + random.nextInt(10))
            }
            // 释放图形上下文
            g.dispose()
            return Captcha(strEnsure.toString(), SKImage(image, "image/jpeg"))
        }
    }
}