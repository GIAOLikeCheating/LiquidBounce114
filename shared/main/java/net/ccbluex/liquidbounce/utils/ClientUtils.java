/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.network.login.server.ISPacketEncryptionRequest;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.security.PublicKey;

@SideOnly(Side.CLIENT)
public final class ClientUtils extends MinecraftInstance {

    private static final Logger logger = LogManager.getLogger("LiquidBounce");

    public static Logger getLogger() {
        return logger;
    }

    public static void disableFastRender() {
        LiquidBounce.wrapper.getFunctions().disableFastRender();
    }

    public static void sendEncryption(final INetworkManager networkManager, final SecretKey secretKey, final PublicKey publicKey, final ISPacketEncryptionRequest encryptionRequest) {
        networkManager.sendPacket(classProvider.createCPacketEncryptionResponse(secretKey, publicKey, encryptionRequest.getVerifyToken()), () -> {
            networkManager.enableEncryption(secretKey);

            return null;
        });
    }
    public static WVec3 getVectorForRotation(float p_getVectorForRotation_1_, float p_getVectorForRotation_2_) {
        float f = (float) Math.cos(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f1 = (float) Math.sin(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f2 = (float) Math.cos(-p_getVectorForRotation_1_ * 0.017453292F);
        float f3 = (float) Math.sin(-p_getVectorForRotation_1_ * 0.017453292F);
        return new WVec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
    public static void displayChatMessage(final String message) {
        if (mc.getThePlayer() == null) {
            getLogger().info("(MCChat)" + message);
            return;
        }

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);

        mc.getThePlayer().addChatMessage(LiquidBounce.wrapper.getFunctions().jsonToComponent(jsonObject.toString()));
    }
}