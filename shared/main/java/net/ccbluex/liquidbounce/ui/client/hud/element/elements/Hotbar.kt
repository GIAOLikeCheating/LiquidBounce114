package net.ccbluex.liquidbounce.ui.client.hud.element.elements


import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import tomk.hotbarutil
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.util.ITooltipFlag
import java.awt.Color
import kotlin.collections.ArrayList


@ElementInfo(name = "Hotbar") class Hotbar(x: Double = 40.0, y: Double = 100.0) : Element(x, y) {


    val slotlist = mutableListOf<hotbarutil>()

    private var lastSlot = -1

    init {
        for (i in 0..8) {
            val slot = hotbarutil()
            slotlist.add(slot)
        }
    }

    override fun drawElement(): Border {

        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)


        slotlist.forEachIndexed { index, hotbarutil ->

            val hover = index == mc.thePlayer!!.inventory.currentItem && mc.thePlayer!!.inventory.mainInventory[index] != null
            val scale = hotbarutil.translate.x
            val positionX = (index * 25 / scale) - 5
            val currentitem = mc.thePlayer!!.inventory.mainInventory[mc.thePlayer!!.inventory.currentItem]
            val currentitem2 = mc2.player!!.inventory.mainInventory[mc2.player!!.inventory.currentItem]
            hotbarutil.size = if (hover) 1.5f else 1.0f
            hotbarutil.translate.translate(hotbarutil.size, 0f, 2.0)

            if (hover) {
                GlStateManager.pushMatrix()
                GlStateManager.scale(scale - 0.5f, scale - 0.5f, scale - 0.5f)

                try {
                    val list = currentitem2.getTooltip(mc2.player, ITooltipFlag { mc.gameSettings.advancedItemTooltips })
                    val infolist : ArrayList<String> = ArrayList()

                    for(i in 0 until list.size) {
                        if (!infolist.contains(list[i]) && list[i].length > 2 ) {
                            infolist.add(list[i])
                        }
                    }
                    var posy = -13f
                    infolist.forEachIndexed{index , it ->
                        val font = if(ColorUtils.stripColor(infolist[index]) == currentitem!!.displayName) Fonts.font40 else Fonts.font35
                        font.drawString(infolist[index], positionX * 1.5f,-(8.5f * infolist.size) + posy,if(ColorUtils.stripColor(infolist[index]) == currentitem!!.displayName) -1 else Color(175 ,175 ,175).rgb, true)
                        posy += font.fontHeight + 2f
                    }
                    infolist.clear()
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                GlStateManager.popMatrix()
            }
            GlStateManager.pushMatrix()
            GlStateManager.scale(scale, scale, scale)
            RenderHelper.enableGUIStandardItemLighting()
            hotbarutil.renderHotbarItem(index, positionX, -10f, mc.timer.renderPartialTicks)
            RenderHelper.disableStandardItemLighting()
            GlStateManager.popMatrix()
        }

        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
        return Border(0f, 0f, 180f, 17f)
    }
}