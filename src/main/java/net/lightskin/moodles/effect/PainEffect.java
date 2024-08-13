package net.lightskin.moodles.effect;

import java.util.Map;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.lightskin.moodles.Moodles;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PainEffect extends MoodleEffect{
    public static String NAME = "pain";
    public static int ID;
    //be sure to add multiple for amps
    public PainEffect(int id) {
        super(id, false, 0xFFFFFF);
        this.setPotionName("potion." + NAME);
        ID = id;
        moodleFields.put(1, new String[] {"In Pain","Reduced Accuracy."});
        moodleFields.put(2, new String[] {"Debilitated","Low Accuracy."});
        moodleFields.put(3, new String[] {"Agony","Can't attack."});
        //auto add numbers for levels of this effect/moodle
        icons.put(1, new ResourceLocation(Moodles.MODID, "textures/effects/" + NAME + 1 + ".png"));
    }
    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        // Return false to prevent the timer from being displayed
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    public boolean canAmplify() {
        return true;
    }

    @SideOnly(Side.SERVER)
    @Override
    public void performEffect(EntityLivingBase entity, int amplifer) {
        EntityPlayerMP player;
        if (entity instanceof EntityPlayerMP) {
            player = (EntityPlayerMP)entity;
        }
    }
    
    @Override
    public void affectEntity(EntityLivingBase thrower, EntityLivingBase entity, int amplifier, double potency) {
        this.performEffect(entity, amplifier);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isBadEffect() {
        return true;
    }
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int posX, int posY, PotionEffect effect, Minecraft mc) {
    	MoodlesEffects.renderInventoryEffect(posX, posY, effect, mc, icons, moodleFields);
    }
}
