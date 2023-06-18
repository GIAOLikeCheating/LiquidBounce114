/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "Criticals", description = "Automatically deals critical hits.", category = ModuleCategory.COMBAT)
class Criticals : Module() {

    val modeValue = ListValue("Mode", arrayOf("Grim","Grim1","Grim2"), "Grim")
    val delayValue = IntegerValue("Delay", 0, 0, 500)
    private val lookValue = BoolValue("SendC06", false)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    var n = 0
    var attacks = 0
    val msTimer = MSTimer()
    private fun sendCriticalPacket(
        xOffset: Double = 0.0,
        yOffset: Double = 0.0,
        zOffset: Double = 0.0,
        ground: Boolean
    ) {
        val x = mc.thePlayer!!.posX + xOffset
        val y = mc.thePlayer!!.posY + yOffset
        val z = mc.thePlayer!!.posZ + zOffset
        if (lookValue.get()) {
            mc.netHandler.addToSendQueue(
                classProvider.createCPacketPlayerPosLook(
                    x,
                    y,
                    z,
                    mc.thePlayer!!.rotationYaw,
                    mc.thePlayer!!.rotationPitch,
                    ground
                )
            )
        } else {
            mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y, z, ground))
        }
    }


    override fun onEnable() {
        if (modeValue.get().equals("NoGround", ignoreCase = true))
            mc.thePlayer!!.jump()
    }

    @EventTarget
    fun onAttack(event: AttackEvent) {
        if (classProvider.isEntityLivingBase(event.targetEntity)) {
            val thePlayer = mc.thePlayer ?: return
            val entity = event.targetEntity!!.asEntityLivingBase()

            if (!thePlayer.onGround || thePlayer.isOnLadder || thePlayer.isInWeb || thePlayer.isInWater ||
                thePlayer.isInLava || thePlayer.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() ||
                LiquidBounce.moduleManager[Fly::class.java].state || !msTimer.hasTimePassed(delayValue.get().toLong()))
                return

            val x = thePlayer.posX
            val y = thePlayer.posY
            val z = thePlayer.posZ

            when (modeValue.get().toLowerCase()) {
                "grim" -> {
                    attacks++
                    if (attacks > 6) {
                        mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.2, z, false))
                        mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosition(x, y + 0.1216, z, false))
                        attacks = 0
                    }
                }

                "grim1" -> {
                    if (thePlayer.isAirBorne)
                        mc.netHandler.addToSendQueue(
                            (classProvider.createCPacketPlayerPosition(
                                x,
                                y + 0.06250000001304,
                                z,
                                true
                            ) as IPacket)!!
                        )
                    mc.netHandler.addToSendQueue(
                        (classProvider.createCPacketPlayerPosition(
                            x,
                            y + 0.06150000001304,
                            z,
                            false
                        ) as IPacket)!!
                    )
                    this.msTimer.reset();
                }

                "grim2" -> {
                    this.attacks = attacks + 1;
                    if (this.attacks <= 6) {

                    } else {
                        if (thePlayer.onGround)
                            mc.netHandler.addToSendQueue(
                                classProvider.createCPacketPlayerPosition(
                                    x,
                                    y + 0.01,
                                    z,
                                    false
                                )
                            );
                        mc.netHandler.addToSendQueue(
                            classProvider.createCPacketPlayerPosition(
                                x,
                                y + 1.0E-10,
                                z,
                                false
                            )
                        );
                        mc.netHandler.addToSendQueue(
                            classProvider.createCPacketPlayerPosition(
                                x,
                                y + 0.114514,
                                z,
                                false
                            )
                        );
                        this.attacks = 0;
                    }
                }

            }
            msTimer.reset()
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (classProvider.isCPacketPlayer(packet) && modeValue.get().equals("NoGround", ignoreCase = true))
            packet.asCPacketPlayer().onGround = false
    }

    override val tag: String?
        get() = modeValue.get()
}
