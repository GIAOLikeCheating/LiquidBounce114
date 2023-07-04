/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.features.module.modules.tomk.VisualColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer.Companion.assumeNonVolatile
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FontValue
import tomk.render.RoundedUtil
import java.awt.Color

/**
 * CustomHUD effects element
 *
 * Shows a list of active potion effects
 */
@ElementInfo(name = "Effects")
class Effects(x: Double = 2.0, y: Double = 10.0, scale: Float = 1F,
              side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    private val fontValue = FontValue("Font", Fonts.font35)
    private val shadow = BoolValue("Shadow", true)

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        var y = 0F
        var width = 0F

        val fontRenderer = fontValue.get()

        assumeNonVolatile = true

        for (effect in mc.thePlayer!!.activePotionEffects) {
            val potion = functions.getPotionById(effect.potionID)

            val number = when {
                effect.amplifier == 1 -> "II"
                effect.amplifier == 2 -> "III"
                effect.amplifier == 3 -> "IV"
                effect.amplifier == 4 -> "V"
                effect.amplifier == 5 -> "VI"
                effect.amplifier == 6 -> "VII"
                effect.amplifier == 7 -> "VIII"
                effect.amplifier == 8 -> "IX"
                effect.amplifier == 9 -> "X"
                effect.amplifier > 10 -> "X+"
                else -> "I"
            }

            val name = "${functions.formatI18n(potion.name)} $number§f: §7${effect.getDurationString()}"
            val stringWidth = fontRenderer.getStringWidth(name).toFloat()
            val color1 = RenderUtils.getGradientOffset(Color(VisualColor.r.get(), VisualColor.b.get(), VisualColor.g.get()), Color(
                VisualColor.r2.get(), VisualColor.b2.get(), VisualColor.g2.get(),1), (Math.abs(System.currentTimeMillis() / y.toDouble() + 2 * 3) / 10)).rgb

            if (width < stringWidth)
                width = stringWidth

            //fontRenderer.drawString(name, -stringWidth, y, potion.liquidColor, shadow.get())
            //box
            RoundedUtil.drawRoundOutline(40F,40F,-stringWidth,y,3F,2F, Color(255,255,255),Color(color1))
            fontRenderer.drawString(name , -stringWidth , y , 10444702 , shadow.get())



            y -= fontRenderer.fontHeight
        }

        assumeNonVolatile = false

        if (width == 0F)
            width = 40F

        if (y == 0F)
            y = -10F

        return Border(2F, fontRenderer.fontHeight.toFloat(), -width - 2F, y + fontRenderer.fontHeight - 2F)
    }
}