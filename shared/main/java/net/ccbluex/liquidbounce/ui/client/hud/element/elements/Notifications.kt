
package net.ccbluex.liquidbounce.ui.client.hud.element.elements
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.render.RenderUtils

import org.lwjgl.opengl.GL11
import tomk.EaseUtils
import tomk.render.RoundedUtil
import java.awt.Color


/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 0.0, y: Double = 0.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {


    /**
     * Example notification for CustomHUD designer
     */

    private val exampleNotification = Notification("Notification", "This is example", NotifyType.INFO)
    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        val notifications = mutableListOf<Notification>()
        //FUCK YOU java.util.ConcurrentModificationException
        for((index, notify) in LiquidBounce.hud.notifications.withIndex()){
            GL11.glPushMatrix()

            if(notify.drawNotification(index,this.renderX.toFloat(), this.renderY.toFloat(),scale)){
                notifications.add(notify)
            }

            GL11.glPopMatrix()
        }
        for(notify in notifications){
            LiquidBounce.hud.notifications.remove(notify)
        }

        if (classProvider.isGuiHudDesigner(mc.currentScreen)) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
//            exampleNotification.x = exampleNotification.textLength + 8F

            return Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(),0F,0F)
        }

        return null
    }
    fun drawBoarderBlur() {}

}


class Notification(val title: String, val content: String, val type: NotifyType, val time: Int=1000, val animeTime: Int=350) {
    private var s: String? = null
    var n2: Int = Fonts.tenacitybold35.getStringWidth(content)
    var textLength = Math.max(n2 as Int, 0 as Int)
    val width=this.textLength.toFloat() + 80.0f
    val height=30
    var fadeState = FadeState.IN
    var nowY=-height
    var displayTime=System.currentTimeMillis()
    var animeXTime=System.currentTimeMillis()
    var x = 0F

    var animeYTime=System.currentTimeMillis()



    /**
     * Draw notification
     */
    fun drawNotification(index: Int, blurRadius: Float, y: Float, scale: Float):Boolean {

        val realY=-(index+1) * (height + 2)


        val nowTime=System.currentTimeMillis()

        var transY=nowY.toDouble()
        //Y-Axis Animation
        if(nowY!=realY){
            var pct=(nowTime-animeYTime)/animeTime.toDouble()
            if(pct>1){
                nowY=realY
                pct=1.0
            }else{
                pct= EaseUtils.easeOutQuart(pct)
            }
            GL11.glTranslated(0.0,(realY-nowY)*pct,0.0)
        }else{
            animeYTime=nowTime

        }
        GL11.glTranslated(1.0,nowY.toDouble(),0.0)

        //X-Axis Animation
        var pct=(nowTime-animeXTime)/animeTime.toDouble()
        when(fadeState){
            FadeState.IN -> {
                if(pct>1){
                    fadeState= FadeState.STAY
                    animeXTime=nowTime
                    pct=1.0
                }
                pct= EaseUtils.easeOutQuart(pct)
                transY+=(realY-nowY)*pct
            }

            FadeState.STAY -> {
                pct=1.0
                if((nowTime-animeXTime)>time){
                    fadeState= FadeState.OUT
                    animeXTime=nowTime
                }
            }

            FadeState.OUT -> {
                if(pct>1){
                    fadeState= FadeState.END
                    animeXTime=nowTime
                    pct=2.0
                }
                pct=1- EaseUtils.easeInQuart(pct)
            }

            FadeState.END -> {
                return true
            }
        }
        val transX=width-(width*pct)-width
        GL11.glTranslated(width-(width*pct),0.0,0.0)
        GL11.glTranslatef(-width.toFloat(),0F,0F)

        //draw notify
//        GL11.glPushMatrix()
//        GL11.glEnable(GL11.GL_SCISSOR_TEST)

        if (type == NotifyType.SUCCESS)
            s = "SUCCESS";
        else if (type == NotifyType.ERROR)
            s = "ERROR";
        else if (type == NotifyType.WARNING)
            s = "WARNING";
        else if ( type == NotifyType.INFO)
            s = "INFO";

        if (s == "INFO") {
            RoundedUtil.drawRound(38F,0F,width - 50F,28F,4.5F, Color(192,192,192,190))
            Fonts.tenacitycheck60.drawString("m",42F,9F,Color(192,192,192,255).rgb,true)
            Fonts.tenacitybold43.drawString(title,60F,3f,Color.white.rgb,true)
            Fonts.tenacitybold35.drawString(content,60f,16f,Color.white.rgb,true)
        }

        if (s == "WARNING") {
            RoundedUtil.drawRound(38F,0F,width - 50F,18F,4.5F, Color(224,194,30,170))
            Fonts.tenacitycheck60.drawString("r",42F,9F,Color(224,194,30,255).rgb,true)
            Fonts.tenacitybold43.drawString(title,60F,3f,Color.white.rgb,true)
            Fonts.tenacitybold35.drawString(content,60f,16f,Color.white.rgb,true)
        }

        if (s == "SUCCESS") {

            RoundedUtil.drawRound(30F,0F,width - 50F,15F,1F, Color(45 ,45, 45,100))
            Fonts.font35.drawString(content,55f,5f,Color.white.rgb,false)
            val successbu = MinecraftInstance.classProvider.createResourceLocation("liquidbounce/noti/suc.png")
            RenderUtils.drawImage(successbu,35,0,15,15)


        }
        if (s == "ERROR") {
            RoundedUtil.drawRound(30F,0F,width - 50F,15F,1F, Color(45,45,45,100))
            Fonts.font35.drawString(content,55f,5f,Color.white.rgb,false)
            val err = MinecraftInstance.classProvider.createResourceLocation("liquidbounce/noti/err.png")
            RenderUtils.drawImage(err,35,0,15,15)
        }




        return false
    }
}

enum class NotifyType(var renderColor: Color) {
    SUCCESS(Color(255 ,255, 255,200  )),
    ERROR(Color(255 ,255, 255,200  )),
    WARNING(Color(0xF5FD00)),
    INFO(Color(137,214,255));
}


enum class FadeState { IN, STAY, OUT, END }
