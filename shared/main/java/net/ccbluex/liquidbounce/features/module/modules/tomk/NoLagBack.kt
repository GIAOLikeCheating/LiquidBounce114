package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.value.ListValue



@ModuleInfo(name = "NoLagBack", description = "nolag", category = ModuleCategory.TOMK)
class NoLagBack : Module(){

    private val modeValue = ListValue("Mode", arrayOf("AntiCheat"), "AntiCheat")
    private var ticks = 0
    private var a = 0
    private var b=0
    override fun onEnable() {
        ticks = 0
    }
    @EventTarget
    fun onUpdate(){
        when(modeValue.get().toLowerCase()){
            "anticheat"->{
                if(ticks > 1000){
                    if(mc.thePlayer!!.isOnLadder&&mc.gameSettings.keyBindJump.pressed){
                        mc.thePlayer!!.motionY=0.11
                    }
                }
                if(ticks > 2000){
                    ticks = 0
                }else{
                    ticks++
                }
            }

        }
    }
    override val tag: String
        get() = modeValue.get().toString()

}