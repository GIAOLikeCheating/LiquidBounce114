package net.ccbluex.liquidbounce.features.module.modules.tomk
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.*

import java.util.*
import javax.print.attribute.standard.Chromaticity.COLOR


@ModuleInfo(name = "VisualColor", category = ModuleCategory.TOMK, array = false, description = "idk")
class VisualColor: Module() {
    companion object {
        public val WhichColors = ListValue("WhichColor", arrayOf("Default","Purple"), "Default")
        val blur = BoolValue("blur", false)
        val Shadow = BoolValue("Shadow", false)
        val r= IntegerValue("ClientRed", 0, 0, 255)
        val b = IntegerValue("ClientGreen", 255, 0, 255)
        val g = IntegerValue("ClientBlue", 255, 0, 255)
        val r2 = IntegerValue("ClientRed2", 255, 0, 255)
        val b2 = IntegerValue("ClientGreen2", 40, 0, 255)
        val g2 = IntegerValue("ClientBlue2", 255, 0, 255)
        val gradientSpeed = IntegerValue("DoubleColor-Speed", 100, 10, 1000)
        @EventTarget
        fun onEnable(){
            if (WhichColors.get() == "Default"){
                r.set(175)
                g.set(0)
                b.set(175)
                r2.set(0)
                g2.set(200)
                b2.set(195)
                return

            }

        }
    }

}






