package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.SPacketChat

@ModuleInfo(name = "AutoGG", category = ModuleCategory.TOMK, description = "GG")
class AutoGG : Module() {
    private val textValue = TextValue("Text", "GG")
    var totalPlayed = 0
    var win = 0
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()

        if (packet is SPacketChat) {
            val text = packet.chatComponent.unformattedText

            if (text.contains("Congratulations", true)) {
                mc.thePlayer!!.sendChatMessage(textValue.get())
                win++

            }
            if (text.contains("Game Started", true)) {
                totalPlayed++
            }
        }
    }
    override val tag: String
        get() = "HuaYuTing"
}