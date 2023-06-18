package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import net.ccbluex.liquidbounce.features.module.modules.tomk.AutoL
import net.ccbluex.liquidbounce.features.module.modules.tomk.VisualColor
import net.ccbluex.liquidbounce.feng.FontDrawer
import net.ccbluex.liquidbounce.feng.FontLoaders
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11
import tomk.CFont.CFontRenderer
import tomk.Recorder
import tomk.blur.BlurBuffer
import tomk.render.RoundedUtil
import java.awt.Color
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.roundToLong
@ElementInfo(name = "StatusBar")
class StatusBar(
) : Element() {
    val fontValue = FontValue("Font", Fonts.tenacitybold35)
    private var easingValue = 0
    private var easingHealth = 0f
    private var easingFood = 0f
    private var easingaromor = 0f
    private var easingExp= 0f
    override fun drawElement(): Border {
        val fontRenderer = fontValue.get()
        //血量
        fontRenderer.drawString("Health:" + mc.thePlayer!!.health,2f, fontRenderer.fontHeight * 3f + 8f, Color.WHITE.rgb,true)
        RoundedUtil.drawRound(
                37F,
                25.5F,
                easingHealth / mc.thePlayer!!.maxHealth * 90.0f,
                6.0f,
                2F,
                Color(0, 95, 255)
        )
        //图标
        Fonts.ico2.drawString("s",370F,37F,Color(192,192,192,255).rgb,true)
        return Border(0f, 0f, 10F,20F)
    }
}