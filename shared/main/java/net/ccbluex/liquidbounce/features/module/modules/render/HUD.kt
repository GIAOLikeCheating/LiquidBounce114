/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.render

import tomk.ColorSetting
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.feng.FontDrawer
import net.ccbluex.liquidbounce.feng.FontLoaders
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.gui.ScaledResolution
import tomk.ColorUtil
import tomk.render.RoundedUtil
import java.awt.Color
import java.text.DecimalFormat
import kotlin.math.pow

@ModuleInfo(name = "HUD", description = "Toggles visibility of the HUD.", category = ModuleCategory.RENDER, array = false)
class HUD : Module() {
    @JvmField
    val colorMode = ListValue(
            "Hud Mode",
            arrayOf("Double Color", "Tenacity", "Light Rainbow", "Rainbow", "Static", "Fade", "Double Color", "Analogous"),
            "Tenacity"
    )
    private val hotbar = BoolValue("Hotbar", false)
    val ColorItem = BoolValue("HotbarRect", true)
    val hotbarEaseValue = BoolValue("HotbarEase", true)
    val inventoryParticle = BoolValue("InventoryParticle", false)
    val hideHotBarValue = BoolValue("HideHotbar", false)
    val betterHotbarValue = BoolValue("BetterHotbar", true)
    val fontChatValue = BoolValue("FontChat", false)
    val chatRect = BoolValue("ChatRect", false)
    val chatAnimValue = BoolValue("ChatAnimation", true)
    private val RenderArmor = BoolValue("RenderArmor", true)
    private val OtherRender = BoolValue("OtherRender", true)
    val blurValue = BoolValue("Blur", false)
    public val BlurStrength = FloatValue("BlurStrength", 15F, 0f, 30F)//这是模糊°
    val Radius = IntegerValue("BlurRadius", 10 , 1 , 50 )
    var High1123 = 0.0
    private var easingValue = 0
    private var easingHealth = 0f
    private var easingFood = 0f
    private var easingaromor = 0f
    private var easingExp= 0f
    @kotlin.jvm.JvmField
    val hueInterpolation = BoolValue("Hue Interpolate", false)
    val degree = FloatValue("Degree", -30f, 1f, 30f)
    val colorAlt = ColorSetting("Alt Color",getAlternateClientColor()) { true }
    val color: ColorSetting = ColorSetting("Main Color", getClientColor()) { true }
    val movingColors = BoolValue("Moving Colors", false)
    private val decimalFormat:DecimalFormat = DecimalFormat()
    fun getHotbar(): BoolValue {
        return hotbar
    }

    val sr = ScaledResolution(mc2)
    val left: Int = sr.getScaledWidth() / 2 + 91
    val top: Int = sr.getScaledHeight() - 100
    val x = 380
    @EventTarget
    fun onRender2D(event: Render2DEvent?) {
        if (classProvider.isGuiHudDesigner(mc.currentScreen))
            return
        if (!(this.hotbar.get() && mc.thePlayer != null && mc.theWorld != null)) {
            var color = Color(252, 83, 86)
            if (this.easingHealth <= 0.0f) {
                this.easingHealth = 0.0f
            }
            if (this.easingHealth >= 20.0f) {
                this.easingHealth = 20.0f
            }
            if (this.easingFood <= 0.0f) {
                this.easingFood = 0.0f
            }
            if (this.easingFood >= 20.0f) {
                this.easingFood = 20.0f
            }


            if (mc.thePlayer!!.isPotionActive(classProvider.getPotionEnum(PotionType.REGENERATION))) {
                color = Color(244, 143, 177)
            }
            val f: Float = x.toFloat()
            var f1: Float

            RoundedUtil.drawRound(480f, 418f, 90F,6F,  0f, Color(0, 0, 0,200))

            RoundedUtil.drawRound(480f, 418f, easingHealth / mc.thePlayer!!.maxHealth * 90.0f, 6.0f,0f,  color)
            var cfontrenderer: FontDrawer = FontLoaders.C16
            val stringbuilder = StringBuilder()
            val decimalformat: DecimalFormat = this.decimalFormat
            val f3: Float = this.easingHealth

            var s =
                    stringbuilder.append(decimalformat.format(java.lang.Float.valueOf(f3 / mc.thePlayer!!.maxHealth * 100f)))
                            .append("%").toString()

            var f2: Float = 480f

            var i: Int =424
            var cfontrenderer2: FontDrawer? = FontLoaders.C16
            cfontrenderer!!.drawString(s, f2, (i - cfontrenderer2!!.height / 2).toFloat(), -1)

            RoundedUtil.drawRound(380f, 428f, 90F,6F,  0f, Color(0, 0, 0,200))
            RoundedUtil.drawRound(
                    380f,
                    428f,
                    this.easingFood / 20.0f * 90.0f,
                    6.0f,
                    0f,
                    Color(255, 235, 100)
            )
            cfontrenderer = FontLoaders.C16
            s = this.decimalFormat.format(java.lang.Float.valueOf(this.easingFood / 20.0f * 100f)) + "%"

            f2 = 380f
            i = 452
            cfontrenderer2 = FontLoaders.C16
            cfontrenderer.drawString(s, f2, (i - cfontrenderer2.getHeight() / 2).toFloat() - 3.0f - 15.0f, -1)

            RoundedUtil.drawRound(380f, 418f, 90F,6F,  0f, Color(0, 0, 0,200))
            RoundedUtil.drawRound(
                    380f,
                    418f,
                    this.easingaromor / 20.0f * 90.0f,
                    6.0f,
                    0f,
                    Color(10, 100, 255)
            )
            cfontrenderer = FontLoaders.C16
            s = this.decimalFormat.format(java.lang.Float.valueOf(this.easingaromor / 20.0f * 100f)) + "%"
            f2 = 380f
            i = 424
            cfontrenderer2 = FontLoaders.C16
            cfontrenderer.drawString(s, f2, (i - cfontrenderer2.getHeight() / 2).toFloat(), -1)
            RoundedUtil.drawRound(480f, 428f, 90F,6F,  0f, Color(0, 0, 0,200))
            RoundedUtil.drawRound(
                    480f,
                    428f ,
                    this.easingExp  * 90.0f,
                    6.0f,
                    0f,
                    Color(60, 255, 10)
            )
            cfontrenderer = FontLoaders.C16
            s = this.decimalFormat.format(java.lang.Float.valueOf(mc2.player.experienceLevel.toFloat())) + "EXP"
            f2 = 480f
            i = 452
            cfontrenderer2 = FontLoaders.C16
            cfontrenderer.drawString(s, f2, (i - cfontrenderer2.getHeight() / 2) - 3.0f - 15.0f, -1)
            f1 = this.easingHealth
            f2 = mc.thePlayer!!.health - this.easingHealth
            var f5 = 2.0f
            var f6 = 7.0f
            var f7 = f2
            var f8 = f1
            var flag = false
            var f9 = Math.pow(f5.toDouble(), f6.toDouble()).toFloat()
            this.easingHealth = f8 + f7 / f9 * RenderUtils.deltaTime.toFloat()
            f1 = this.easingFood
            f2 = mc2.player.foodStats.foodLevel.toFloat() - this.easingFood
            f5 = 2.0f
            f6 = 7.0f
            f7 = f2
            f8 = f1
            f9 = f5.toDouble().pow(f6.toDouble()).toFloat()
            this.easingFood = f8 + f7 / f9 * RenderUtils.deltaTime.toFloat()
            f1 = this.easingaromor
            f2 = mc2.player.totalArmorValue.toFloat() - this.easingaromor
            f5 = 2.0f
            f6 = 7.0f
            f7 = f2
            f8 = f1
            flag = false
            f9 = f5.toDouble().pow(f6.toDouble()).toFloat()
            this.easingaromor = f8 + f7 / f9 * RenderUtils.deltaTime.toFloat()
            f1 = this.easingExp
            f2 = mc2.player.experience .toFloat() - this.easingExp
            f5 = 2.0f
            f6 = 7.0f
            f7 = f2
            f8 = f1
            flag = false
            f9 = f5.toDouble().pow(f6.toDouble()).toFloat()
            this.easingExp = f8 + f7 / f9 * RenderUtils.deltaTime.toFloat()
        }
        if (!mc2.ingameGUI.chatGUI.chatOpen) {
            if (OtherRender.get()) {
                High1123 = if (mc2.ingameGUI.chatGUI.chatOpen) {
                    (RenderUtils.height() - 35).toDouble()
                } else {
                    if (mc.playerController.isNotCreative && RenderArmor.get()) {
                        onArmor(mc.thePlayer!!) }
                    (RenderUtils.height() - 20).toDouble()
                }
                (RenderUtils.height() - 20).toDouble()
            }
        }
        LiquidBounce.hud.render(false)
    }

    fun getClientColor(): Color {
        return Color(7, 255, 247, 255)
    }
    fun getAlternateClientColor(): Color {
        return Color(255, 255, 255, 255)
    }
    private fun mixColors(color1: Color, color2: Color): Color? {
        return if (movingColors.get()) {
            ColorUtil.interpolateColorsBackAndForth(15, 1, color1, color2, hueInterpolation.get())
        } else {
            ColorUtil.interpolateColorC(color1, color2, 0F)
        }
    }
    fun getClientColors(): Array<Color>? {
        val firstColor: Color
        val secondColor: Color
        when (colorMode.get()) {
            "Tenacity" -> {
                firstColor = mixColors(getClientColor(),getAlternateClientColor())!!
                secondColor = mixColors(getAlternateClientColor(),getClientColor())!!
            }

            "Light Rainbow" -> {
                firstColor = ColorUtil.rainbow(15, 1, .6f, 1, 1)
                secondColor = ColorUtil.rainbow(15, 40, .6f, 1F, 1)
            }

            "Rainbow" -> {
                firstColor = ColorUtil.rainbow(15, 1, 1, 1, 1)
                secondColor = ColorUtil.rainbow(15, 40, 1, 1, 1)
            }

            "Static" -> {
                firstColor = color.getColor()!!
                secondColor = firstColor
            }

            "Fade" -> {
                firstColor = ColorUtil.fade(15, 1, color.getColor(), 1F)
                secondColor = ColorUtil.fade(15, 50, color.getColor(), 1F)
            }

            "Double Color" -> {
                firstColor = mixColors(color.getColor()!!, colorAlt.getColor()!!)!!
                secondColor = mixColors(colorAlt.getColor()!!, color.getColor()!!)!!
            }

            "Analogous" -> {
                val `val` = if (degree.get()== 30F) 0 else 1
                val analogous: Color = ColorUtil.getAnalogousColor(color.getColor()).get(`val`)
                firstColor = color.getColor()?.let { mixColors(it, analogous) }!!
                secondColor = mixColors(analogous, color.getColor()!!)!!
            }

            else -> {
                firstColor = Color(-1)
                secondColor = Color(-1)
            }
        }
        return arrayOf(firstColor, secondColor)
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        LiquidBounce.hud.update()
    }
    fun getHotbarEasePos(x: Int): Int {
        if (!state || !hotbarEaseValue.get()) return x
        easingValue = x
        return easingValue
    }
    private fun onArmor(target: IEntityLivingBase) {
    }
    @EventTarget
    fun onKey(event: KeyEvent) {
        LiquidBounce.hud.handleKey('a', event.key)
    }

    @EventTarget(ignoreCondition = true)
    fun onScreen(event: ScreenEvent) {
        if (mc.theWorld == null || mc.thePlayer == null) return
        if (state && blurValue.get() && !mc.entityRenderer.isShaderActive() && event.guiScreen != null &&
                !(classProvider.isGuiChat(event.guiScreen) || classProvider.isGuiHudDesigner(event.guiScreen))) mc.entityRenderer.loadShader(classProvider.createResourceLocation("liquidbounce" + "/blur.json")) else if (mc.entityRenderer.shaderGroup != null &&
                mc.entityRenderer.shaderGroup!!.shaderGroupName.contains("liquidbounce/blur.json")) mc.entityRenderer.stopUseShader()
    }

    init {
        state = true
    }
}