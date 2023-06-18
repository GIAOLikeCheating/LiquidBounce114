/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock
import net.ccbluex.liquidbounce.api.minecraft.potion.PotionType
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.utils.MovementUtils
import tomk.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.CPacketConfirmTransaction
import net.minecraft.network.play.server.*
import java.util.*

@ModuleInfo(name = "Velocity2", description = "Grim", category = ModuleCategory.COMBAT)
class Velocity2 : Module() {
    /**
     * OPTIONS
     */
     var modeValue = ListValue("Mode", arrayOf("Custom","Grim1","Grim2","Grim3","GrimJump"), "Grim1")

    public var block: IBlock? = null
    private val noFireValue = BoolValue("noFire", false)

    //Custom
    private val customX = FloatValue("Custom-X",0F,0F,1F)
    private val customYStart = BoolValue("CanCustomY",false)
    private val customY = FloatValue("Custom-Y",1F,1F,2F)
    private val customZ = FloatValue("Custom-Z",0F,0F,1F)
    private val customC06FakeLag = BoolValue("Custom-C06FakeLag",false)

    private val i = LinkedList<Packet<INetHandlerPlayClient>>()
    private val j = LinkedList<Packet<INetHandlerPlayServer>>()


    private var d = 0
    private var e = 0
    private var h = 0
    private var k = 8



    private var velocityTimer = MSTimer()
    override val tag: String
        get() = modeValue.get()

    override fun onDisable() {
        mc.thePlayer?.speedInAir = 0.02F
            while (i.size > 0) {
                i.poll()?.processPacket(mc2.connection)
            }
            while (j.size > 0) {
                val g = j.poll() ?: continue
                PacketUtils.sendPacketNoEvent(g)
            }
            e = 0
            j.clear()
            i.clear()
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val thePlayer = mc.thePlayer ?: return

        if (thePlayer.isInWater || thePlayer.isInLava || thePlayer.isInWeb)
            return

        if (noFireValue.get() && mc.thePlayer!!.burning) return
        when (modeValue.get().toLowerCase()) {
            "grimf"->{
                if(e > 0){
                    e--
                }
                mc.netHandler ?: return
                if ((!i.isEmpty()&& d == 0)||e>0) {
                    while (i.size > 0) {
                        i.poll()?.processPacket(mc2.connection)
                    }
                }

                if (!j.isEmpty() && d == 0 ) {
                    while (j.size > 0) {
                        val upPacket = j.poll() ?: continue
                        PacketUtils.sendPacketNoEvent(upPacket)
                    }
                }
                h++
                if (k > 0) {
                    if (h >= 0 || h >= k) {
                        h = 0
                        if (d > 0){
                            d--
                        }
                    }
                }
            }
            "grimb"->{
                val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
                if (killAura.target != null && mc.thePlayer!!.hurtTime > 0  && mc.thePlayer!!.onGround) {
                    mc.thePlayer!!.jump()
                }else{
                    if (thePlayer.hurtTime > 0) {
                        thePlayer.motionX += -1.0E-7
                        thePlayer.motionY += -1.0E-7
                        thePlayer.motionZ += -1.0E-7
                        thePlayer.isAirBorne = true
                    }
                }
            }
            "grimjump"->{
                val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
                if (killAura.target != null && mc.thePlayer!!.hurtTime > 0  && mc.thePlayer!!.onGround) {
                    mc.thePlayer!!.jump()
                }
                else if (thePlayer.hurtTime > 0 && mc.thePlayer!!.onGround) {
                        thePlayer.motionX += -1.0E-7
                        thePlayer.motionY += -1.0E-7
                        thePlayer.motionZ += -1.0E-7
                        thePlayer.isAirBorne = true

                }
            }
            "grimc"->{
                val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
                if (killAura.target != null && mc.thePlayer!!.hurtTime > 0  && mc.thePlayer!!.onGround) {
                    mc.thePlayer!!.jump()
                }else{
                    if (thePlayer.hurtTime > 0) {
                        thePlayer.motionX += -1.0E-7
                        thePlayer.motionY += -1.0E-7
                        thePlayer.motionZ += -1.0E-7
                        thePlayer.isAirBorne = true
                    }
                }
            }
            "grim2" -> {
                if (thePlayer.hurtTime > 0 && !thePlayer.isDead && !thePlayer.onGround) {
                    if (!thePlayer.isPotionActive(classProvider.getPotionEnum(PotionType.MOVE_SPEED))) {
                        thePlayer.motionX *= 0.451145F
                        thePlayer.motionZ *= 0.451145F
                    }
                }



            }
            "custom" -> {
                if (thePlayer.hurtTime > 0 && !thePlayer.isDead && !mc.thePlayer!!.isPotionActive(classProvider.getPotionEnum(PotionType.MOVE_SPEED)) && !mc.thePlayer!!.isInWater) {
                    thePlayer.motionX *= customX.get()
                    thePlayer.motionZ *= customZ.get()
                    if(customYStart.get())thePlayer.motionY /= customY.get()
                    if (customC06FakeLag.get()) mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerPosLook(thePlayer.posX, thePlayer.posY, thePlayer.posZ, thePlayer.rotationYaw, thePlayer.rotationPitch, thePlayer.onGround))
                }
            }

        }


    }

    override fun onEnable() {
        if (modeValue.get().equals("GrimF")) {
            i.clear()
            j.clear()
            d = 0
            e = 0
        }
    }

    @EventTarget
    fun onBlockBB(event: BlockBBEvent) {
        block = event.block
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {

    }
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val f = mc.thePlayer ?: return
        val a = event.packet.unwrap()
        val b = event.packet
        if (classProvider.isSPacketEntityVelocity(b)) {
            val packetEntityVelocity = b.asSPacketEntityVelocity()


            if (noFireValue.get() && mc.thePlayer!!.burning) return
            if ((mc.theWorld?.getEntityByID(packetEntityVelocity.entityID) ?: return) != f)
                return

            velocityTimer.reset()

            if (modeValue.get().equals("GrimF")) {
            if (a is SPacketPlayerPosLook) {
                e = 37
            }
            if ((!f.onGround) || (!MovementUtils.isMoving) || e != 0) {
                return
            }
            if (classProvider.isSPacketEntityVelocity(b)) {
                val packetEntityVelocity = b.asSPacketEntityVelocity()

                if (((mc.theWorld?.getEntityByID(packetEntityVelocity.entityID) ?: return) != f))
                    return
                event.cancelEvent()
                if (f.onGround) {
                    d = 5
                } else {
                    d = 9
                }
            }
            if (a !is SPacketConfirmTransaction && (a::class.java!!.getSimpleName()
                    .startsWith("S", true)) && (d > 0)
            ) {
                if ((mc.theWorld?.getEntityByID(b.asSPacketEntityVelocity().entityID)
                        ?: return) == f
                ) {
                    return
                }
                event.cancelEvent()
                i.add(a as Packet<INetHandlerPlayClient>)
                d--
            }
            if (a is SPacketConfirmTransaction && (d > 0)) {
                event.cancelEvent()
            }
            if (((d > 0)) && ((a is SPacketEntity.S17PacketEntityLookMove) || (a is SPacketEntity.S16PacketEntityLook) || (a is SPacketEntity.S15PacketEntityRelMove) || (a is SPacketEntityAttach) || (a is SPacketEntityTeleport) || (a is SPacketEntity) || (a is SPacketEntityVelocity && (mc.theWorld?.getEntityByID(
                    SPacketEntityVelocity().entityID
                ) ?: return) != f))
            ) {
                event.cancelEvent()
                i.add(a as Packet<INetHandlerPlayClient>)
                if (a is SPacketConfirmTransaction && ((d > 0))) {
                    event.cancelEvent()
                    if (d <= 3) {
                        i.add(a as Packet<INetHandlerPlayClient>)
                    }
                    d--
                }
                if (a !is CPacketConfirmTransaction && a::class.java!!.getSimpleName()
                        .startsWith("C", true) && (d > 0)
                ) {
                    event.cancelEvent()
                    d--
                    j.add(a as Packet<INetHandlerPlayServer>)
                }
                if (a is SPacketConfirmTransaction && (d > 0)) {
                    event.cancelEvent()
                }
            }
        }
        }
    }

    @EventTarget
    fun onJump(event: JumpEvent) {
        val thePlayer = mc.thePlayer

        if (thePlayer == null || thePlayer.isInWater || thePlayer.isInLava || thePlayer.isInWeb)
            return

    }
    }
