package net.ccbluex.liquidbounce.features.module.modules.hyt

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.RotationUtils


@ModuleInfo(name="ScaffoldHelper",  description = "sca",  category = ModuleCategory.TOMK)
class ScaffoldHelper : Module(){


    @EventTarget
    fun onUpdate(event: UpdateEvent) {


        val thePlayer = mc.thePlayer ?: return

        val scaffold = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
        scaffold.state = !thePlayer.onGround
    }
    @EventTarget
    fun onMotion(event: MotionEvent) {
                mc.thePlayer!!.rotationPitch = 25F

            }

override fun onDisable() {
    val scaffold = LiquidBounce.moduleManager.getModule(Scaffold::class.java) as Scaffold
    scaffold.state = false
}






















}