package net.lightskin.moodles.effect;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.lightskin.moodles.Moodles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class MoodlesEffects {
	public static final int adrenalineEffectId = 2001;
	public static final int brokenBonesEffectId = 2002;
	public static final int painEffectId = 2003;
	public static AdrenalineEffect adrenalineEffect;
	public static BrokenBonesEffect brokenBonesEffect;
	public static PainEffect painEffect;
	public static Map<Integer, MoodleEffect> easyAccess = Maps.newHashMap();
	public MoodlesEffects() {
		adrenalineEffect = new AdrenalineEffect(adrenalineEffectId);
		brokenBonesEffect = new BrokenBonesEffect(brokenBonesEffectId);
		painEffect = new PainEffect(painEffectId);
		easyAccess.put(adrenalineEffectId, adrenalineEffect);
		easyAccess.put(brokenBonesEffectId, brokenBonesEffect);
		easyAccess.put(painEffectId, painEffect);
	}
	//todo: custom moodle 'look'
	public static void renderInventoryEffect(int posX, int posY, PotionEffect effect, Minecraft mc, Map<Integer, ResourceLocation> icons, Map<Integer, String[]> moodleFields) {
        int amp = Math.abs(effect.getAmplifier()); //use this to change texture depending on amp like project zomboid
        //mc.renderEngine.bindTexture(new ResourceLocation(Moodles.MODID, "textures/effects/background" + amp + ".png"));
        //GL11.glEnable(GL11.GL_BLEND);
        //mc.currentScreen.drawTexturedModalRect(posX - 6, posY - 7, 0, 0, 110, 26);
        MoodleEffect tmp = easyAccess.get(effect.getPotionID());
        boolean stand = !tmp.standard, isBad = tmp.isBadEffect();
        if(!stand)
        	mc.renderEngine.bindTexture(icons.get(amp));
        else
        	mc.renderEngine.bindTexture(new ResourceLocation(Moodles.MODID, "textures/effects/" + (isBad ? "bad" : "good")  + amp + ".png"));
        GL11.glEnable(GL11.GL_BLEND);
        //get a 'tool tip' loc? temp change 2001 to 29 to test offset?
    	GL11.glPushMatrix();
        
        // Translate to the position where you want to draw the texture
        GL11.glTranslatef(posX, posY, 0);
        
        // Scale the texture down to fit 18x18
        float scaleX = 18.0f / 256;
        float scaleY = 18.0f / 256;
        GL11.glScalef(scaleX, scaleY, 1.0f);
    	mc.currentScreen.drawTexturedModalRect(12, 12, 0, 0, 256, 256);
    	if(stand) {
    		mc.renderEngine.bindTexture(icons.get(1)); //for standard ones, they just have one arraylist entry
    		mc.currentScreen.drawTexturedModalRect(12, 12, 0, 0, 256, 256);
    	}
    	GL11.glPopMatrix(); //restore state
    	mc.fontRenderer.drawString(moodleFields.get(amp)[0], posX + 24, posY + 6, 0xFFFFFF, true);
    	mc.fontRenderer.drawString(moodleFields.get(amp)[1], posX + 24, posY + 18, 0xEEAAEE, true);
	}
	public static void renderInventoryEffect(int posX, int posY, PotionEffect effect, Minecraft mc, Map<Integer, ResourceLocation> icons) {
        int amp = Math.abs(effect.getAmplifier()); //use this to change texture depending on amp like project zomboid
        //mc.renderEngine.bindTexture(new ResourceLocation(Moodles.MODID, "textures/effects/background" + amp + ".png"));
        //GL11.glEnable(GL11.GL_BLEND);
        //mc.currentScreen.drawTexturedModalRect(posX - 6, posY - 7, 0, 0, 110, 26);
        MoodleEffect tmp = easyAccess.get(effect.getPotionID());
        boolean stand = tmp.standard, isBad = tmp.isBadEffect();
        if(!stand)
        	mc.renderEngine.bindTexture(icons.get(amp));
        else
        	mc.renderEngine.bindTexture(new ResourceLocation(Moodles.MODID, "textures/effects/" + (isBad ? "bad" : "good")  + amp + ".png"));
        GL11.glEnable(GL11.GL_BLEND);
        //get a 'tool tip' loc? temp change 2001 to 29 to test offset?
    	GL11.glPushMatrix();
        
        // Translate to the position where you want to draw the texture
        GL11.glTranslatef(posX, posY, 0);
        
        // Scale the texture down to fit 18x18
        float scaleX = 18.0f / 256;
        float scaleY = 18.0f / 256;
        GL11.glScalef(scaleX, scaleY, 1.0f);
    	drawTexturedModalRect(12, 8, 0, 0, 256, 256);
    	if(stand) {
    		mc.renderEngine.bindTexture(icons.get(1)); //for standard ones, they just have one arraylist entry
    		drawTexturedModalRect(12, 12, 0, 0, 256, 256);
    	}
    	GL11.glPopMatrix(); //restore state
	}
	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
	    float f = 1.0F / 256.0F;
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.startDrawingQuads();
	    tessellator.addVertexWithUV(x, y + height, 0, textureX * f, (textureY + height) * f);
	    tessellator.addVertexWithUV(x + width, y + height, 0, (textureX + width) * f, (textureY + height) * f);
	    tessellator.addVertexWithUV(x + width, y, 0, (textureX + width) * f, textureY * f);
	    tessellator.addVertexWithUV(x, y, 0, textureX * f, textureY * f);
	    tessellator.draw();
	}
	public static void drawMoodleContainer(float x, float y, float width, float height, float lineWidth) {
		GL11.glPushMatrix();
		GL11.glLineWidth(lineWidth);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // Disable depth testing
		GL11.glColor3f(1.0f, 0, 0f); // Set the color to red
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Re-enable depth testing
		GL11.glPopMatrix();
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // Reset color to white

    }
}
