
package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.features.module.modules.tomk.VisualColor
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import java.awt.Color
import tomk.render.RoundedUtil


class GuiMainMenu : WrappedGuiScreen() {

        val Wiz = classProvider.createResourceLocation("liquidbounce/thewiz.png")
    val color4 = RenderUtils.getGradientOffset(
        Color(VisualColor.r.get(), VisualColor.b.get(), VisualColor.g.get()),
        Color(VisualColor.r2.get(), VisualColor.b2.get(), VisualColor.g2.get()),
        (kotlin.math.abs(
            System.currentTimeMillis() / VisualColor.gradientSpeed.get()
                .toDouble() + (1 / 1)
        ) / 10)
    ).rgb
    val Wizb = classProvider.createResourceLocation("liquidbounce/background.png")
    override fun initGui() {
        val defaultHeight = representedScreen.height / 4 + 48
        representedScreen.buttonList.add(classProvider.createGuiButton(1, representedScreen.width / 2 - 100, defaultHeight, 98, 68, functions.formatI18n("menu.singleplayer")))
        representedScreen.buttonList.add(classProvider.createGuiButton(2, representedScreen.width / 2 + 2, defaultHeight, 98, 90, functions.formatI18n("menu.multiplayer")))
        representedScreen.buttonList.add(classProvider.createGuiButton(4, representedScreen.width / 2 + 2, defaultHeight + 24 * 4, 98, 20, functions.formatI18n("menu.quit")))
        representedScreen.buttonList.add(classProvider.createGuiButton(0, representedScreen.width / 2 - 100, defaultHeight + 72, 98, 46, functions.formatI18n("menu.options")))
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        //representedScreen.drawBackground(9)
        representedScreen.drawDefaultBackground()

        //RenderUtils.drawRect(representedScreen.width / 2.0f - 115, representedScreen.height / 4.0f + 35, representedScreen.width / 2.0f + 115, representedScreen.height / 4.0f + 175, Integer.MIN_VALUE)

        //Fonts.fontBold180.drawCenteredString(LiquidBounce.CLIENT_NAME, representedScreen.width / 2F, representedScreen.height / 8F, 4673984, true)
        //Fonts.fontBold180.drawCenteredString("ByteWiz", representedScreen.width / 2F, representedScreen.height / 8F, color4, false)
        RenderUtils.drawImage(Wiz,representedScreen.width / 11 * 5  ,representedScreen.height / 8,80,80)
        //Fonts.font35.drawCenteredString("b" + LiquidBounce.CLIENT_VERSION, representedScreen.width / 2F + 148, representedScreen.height / 8F + Fonts.font35.fontHeight, 0xffffff, true)

        representedScreen.superDrawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: IGuiButton) {
        when (button.id) {
            0 -> mc.displayGuiScreen(classProvider.createGuiOptions(this.representedScreen, mc.gameSettings))
            1 -> mc.displayGuiScreen(classProvider.createGuiSelectWorld(this.representedScreen))
            2 -> mc.displayGuiScreen(classProvider.createGuiMultiplayer(this.representedScreen))
            4 -> mc.shutdown()
            101 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiServerStatus(this.representedScreen)))
            102 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiBackground(this.representedScreen)))
            103 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiModsMenu(this.representedScreen)))
            108 -> mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiContributors(this.representedScreen)))
        }
    }
}