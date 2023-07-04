package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.features.module.modules.tomk.VisualColor
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import tomk.Stencil
import tomk.render.RoundedUtil
import java.awt.Color
import kotlin.math.abs
import ad.utils.Color.modules.CustomUI
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
class ByteWiz(inst: Target): TargetStyle("ByteWiz", inst, true) {

    private var lastTarget: IEntityLivingBase? = null
    override fun drawTarget(entity: IEntityLivingBase) {

        val font = Fonts.sfbold35
        val healthString = "${decimalFormat2.format(entity.health)} "

        if (entity != lastTarget || easingHealth < 0 || easingHealth > entity.maxHealth ||
                abs(easingHealth - entity.health) < 0.01) {
            easingHealth = entity.health
        }
        val width = (76 + Fonts.sfbold40.getStringWidth(entity.name!!))
                .coerceAtLeast(118)
                .toFloat()
        val playerInfo = mc.netHandler.getPlayerInfo(entity.uniqueID)
        val color4 = RenderUtils.getGradientOffset(
            Color(VisualColor.r.get(), VisualColor.b.get(), VisualColor.g.get()),
            Color(VisualColor.r2.get(), VisualColor.b2.get(), VisualColor.g2.get()),
            (kotlin.math.abs(
                System.currentTimeMillis() / VisualColor.gradientSpeed.get()
                    .toDouble() + (1 / 2) * width
            ) / 10)
        ).rgb
        // outline
        val color1 = RenderUtils.getGradientOffset(Color(VisualColor.r.get(),VisualColor.b.get(),VisualColor.g.get()), Color(VisualColor.r2.get(), VisualColor.b2.get(), VisualColor.g2.get(),1), (Math.abs(System.currentTimeMillis() / 200.toDouble() + 1 * width) / 10)).rgb
        // Health
        RoundedUtil.drawRound(
                5F,
                38F,
                (width),
                4F,
                0.2F,
                Color(0,0,0,80)
        )
        updateAnim(entity.health)
        //bar
        RoundedUtil.drawRound(
            0F,
            0F,
            width + 28F,
            50F,
            2F,
            Color(0,0,0,60)
        )
        // Health
        RoundedUtil.drawRound(
            5F,
            38F,
            (width) * (easingHealth / entity.maxHealth),
            4F,
            0.2F,
            Color(VisualColor.r.get(),VisualColor.b.get(),VisualColor.g.get())
        )


        // Name
        Fonts.minecraftFont.drawString("Name:" + entity.name!!, 37, 5, getColor(-1).rgb)
        // Disance
        Fonts.minecraftFont.drawString("Distance: ${decimalFormat.format(mc.thePlayer!!.getDistanceToEntityBox(entity))}",37,15,getColor(-1).rgb)
        // HurtTime
        Fonts.minecraftFont.drawString("HurtTime:${decimalFormat2.format(entity.hurtTime)}", 37, 25, getColor(-1).rgb)
        // HP
        Fonts.minecraftFont.drawString("${decimalFormat2.format(entity.health)}",(width) * (easingHealth / entity.maxHealth) + 8F,37F,getColor(-1).rgb)
        // Head
        if (playerInfo != null) {
            Stencil.write(false)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RenderUtils.fastRoundedRect(4F, 4F, 34F, 34F, 0.5F)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            Stencil.erase(true)
            drawHead(playerInfo.locationSkin, 4, 4, 30, 30, 1F - targetInstance.getFadeProgress())
            Stencil.dispose()
        }
        //font.drawStringWithShadow(healthString, 37, 28.5.toInt(), Color(255,255,255).rgb)
        GL11.glPopMatrix()

        GlStateManager.resetColor()

        lastTarget = entity
    }
    override fun handleShadowCut(entity: IEntityLivingBase) = handleBlur(entity)

    override fun handleShadow(entity: IEntityLivingBase) {
        val width = (38 + Fonts.sfbold40.getStringWidth(entity.name!!))
                .coerceAtLeast(118)
                .toFloat()

        RenderUtils.newDrawRect(0F, 0F, width, 32F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: IEntityLivingBase?): Border? {
        entity ?: return Border(0F, 0F, 118F, 32F)
        val width = (38 + Fonts.sfbold40.getStringWidth(entity.name!!))
                .coerceAtLeast(118)
                .toFloat()
        return Border(0F, 0F, width, 32F)
    }

}