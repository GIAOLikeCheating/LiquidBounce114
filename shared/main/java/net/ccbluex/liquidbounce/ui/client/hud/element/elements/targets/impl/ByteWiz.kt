package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import tomk.Stencil
import tomk.render.RoundedUtil
import java.awt.Color
import kotlin.math.abs

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

        // Draw rect box
        RenderUtils.drawRoundedRect(0F, 0F, width, 40F, 3F, targetInstance.bgColor.rgb)
        // Health
        RoundedUtil.drawRound(
                37F,
                15.5F,
                (width - 42) * (easingHealth / entity.maxHealth),
                4F,
                0.8F,
                Color(108, 21, 122)
        )
        updateAnim(entity.health)
        // Name
        Fonts.tenacitybold40.drawString(entity.name!!, 37, 3, getColor(-1).rgb)
        // Head
        if (playerInfo != null) {
            Stencil.write(false)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RenderUtils.fastRoundedRect(4F, 4F, 34F, 34F, 7F)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            Stencil.erase(true)
            drawHead(playerInfo.locationSkin, 4, 4, 30, 30, 1F - targetInstance.getFadeProgress())
            Stencil.dispose()
        }
        // HP
        GL11.glPushMatrix()
        GL11.glScalef(1.3F,1.3F,1.3F)
        Fonts.tenacitybold40.drawString(healthString , 30 , 18.5.toInt() , Color(255,255,255).rgb)
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