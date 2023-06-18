package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.tomk.AutoL
import net.ccbluex.liquidbounce.features.module.modules.tomk.VisualColor
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11
import tomk.Recorder
import tomk.ShadowUtils
import tomk.blur.BlurBuffer
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.hypot
import kotlin.math.roundToLong
@ElementInfo(name = "Session Info")
class SessionInfo(
        x: Double = 15.0, y: Double = 10.0, scale: Float = 1F,
        side: Side = Side(Side.Horizontal.LEFT, Side.Vertical.UP)
) : Element(x, y, scale, side) {
    private val lineValue = BoolValue("Line", true)
    private val blur = BoolValue("Normal-Blur", true)
    val fontValue = FontValue("Font", Fonts.tenacitybold35)
    private fun calculateBPS(): Double {
        val bps = hypot(
                mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
                mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
        ) * mc.timer.timerSpeed * 20
        return (bps * 100.0).roundToLong() / 100.0
    }

    override fun drawElement(): Border {
        val floatX = renderX.toFloat()
        val floatY = renderY.toFloat()
        val fontRenderer = fontValue.get()
        val y2 = fontRenderer.fontHeight * 5 + 35f + 3f
        val x2 = 60f

        if (blur.get()) {
            GL11.glTranslated(-renderX, -renderY, 0.0)
            GL11.glPushMatrix()
            BlurBuffer.blurRoundArea(floatX, floatY, x2,y2,4F)
            GL11.glPopMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
        }
        if (lineValue.get()) {
            RenderUtils.drawGradientSideways(
                    2.22,
                    fontRenderer.fontHeight + 7.0 + 0.0 - 1,
                    x2.toDouble() - 2.22,
                    fontRenderer.fontHeight + 7.0 + 1.16f - 1,
                    Color(VisualColor.r.get(), VisualColor.b.get(), VisualColor.g.get()).rgb,
                    Color(VisualColor.r2.get(), VisualColor.b2.get(), VisualColor.g2.get()).rgb
            )
        }
        val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
        Fonts.tenacitybold40.drawCenteredString("Session", x2 / 2f, 5f, Color.WHITE.rgb, true)
        val autoL = LiquidBounce.moduleManager.getModule(AutoL::class.java) as AutoL
        fontRenderer.drawString("Time:${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}", 2f, fontRenderer.fontHeight * 1.5f + 8f, Color.WHITE.rgb,true)
        fontRenderer.drawString("Health:" + mc.thePlayer!!.health,2f, fontRenderer.fontHeight * 3f + 8f, Color.WHITE.rgb,true)
        fontRenderer.drawString("Speed:" + calculateBPS(),2f, fontRenderer.fontHeight * 4.5f + 8f, Color.WHITE.rgb,true)
        fontRenderer.drawString("Kill:" + autoL.kills(),2f, fontRenderer.fontHeight * 6f + 8f, Color.WHITE.rgb,true)
        fontRenderer.drawString("FPS:" + Minecraft.getDebugFPS(),2f, fontRenderer.fontHeight * 7.5f + 8f, Color.WHITE.rgb,true)
        return Border(0f, 0f, 60f,  85f)
    }
}