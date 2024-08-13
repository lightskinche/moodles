package net.lightskin.moodles;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.lightskin.moodles.effect.MoodlesEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class PlayerHandler {
	Random random = new Random();
	static int tick = 0;
	static float lastHealth = 20;
    @SubscribeEvent
    public void PlayerTick(TickEvent.PlayerTickEvent event) { 	
		PlayerStatus status = PlayerStatus.get(event.player);
    	if(!event.player.worldObj.isRemote) {
    		Minecraft mc = Minecraft.getMinecraft();
    		MovingObjectPosition objectMouseOver = mc.objectMouseOver;
    		if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
    			if(objectMouseOver.entityHit instanceof EntityMob) {
    		        EntityMob entity = (EntityMob)objectMouseOver.entityHit;
    		        //int increaseby = (int)Math.floor((25.0 - event.player.getDistanceSqToEntity(entity) / 10.0));
    		        status.setAdrenaline(status.getCurrentAdrenaline() + 1);
    		     }
    		}
    		else if(random.nextDouble() < 0.0625) {
				status.setAdrenaline(status.getCurrentAdrenaline() - 1);
			}
        if(status.getCurrentAdrenaline() < 85){
        	if(tick % 400 == 0)
        		if(random.nextDouble() < 0.5f) {
        			status.setPain(status.getCurrentPain() - 1);
        		}
        	if(tick % 2800 == 0) {
        		status.setTrauma(status.getCurrentTrauma() + 1);
        	}
        	if(status.getCurrentAdrenaline() < 50) {
        		if(status.getCurrentTrauma() < 6 && status.getCurrentTrauma() > 0) {
        			event.player.addPotionEffect(new PotionEffect(MoodlesEffects.brokenBonesEffectId, 10, -1));
        			status.setPain(Integer.max(status.getCurrentPain(), 6));
        		}
        		else if(status.getCurrentTrauma() == 0) {
        			event.player.addPotionEffect(new PotionEffect(MoodlesEffects.brokenBonesEffectId, 10, -2));
        			status.setPain(Integer.max(status.getCurrentPain(), 12));
        		}
        		int pain = status.getCurrentPain();
        		if(pain > 6 && pain < 12)
        			event.player.addPotionEffect(new PotionEffect(MoodlesEffects.painEffectId, 10, -1));
        		else if(pain > 12 && pain < 18)
        			event.player.addPotionEffect(new PotionEffect(MoodlesEffects.painEffectId, 10, -2));
        		else if(pain > 18)
        			event.player.addPotionEffect(new PotionEffect(MoodlesEffects.painEffectId, 10, -3));
        	}
        	else {
        		event.player.addPotionEffect(new PotionEffect(MoodlesEffects.adrenalineEffectId, 10, -1));
        		event.player.cameraPitch += random.nextDouble() * 10;
        		event.player.cameraYaw += random.nextDouble() * 10;
        	}
    	}
        else {
        	event.player.addPotionEffect(new PotionEffect(MoodlesEffects.adrenalineEffectId, 10, -2));
        	event.player.cameraPitch += random.nextDouble() * 20;
    		event.player.cameraYaw += random.nextDouble() * 20;
        	if(lastHealth < event.player.getHealth() && !event.player.isDead)
        		event.player.setHealth(lastHealth);
        }
        lastHealth = event.player.getHealth();
    	tick++;
    	if(tick > 2881)
    		tick = 0;
    	} else {
    		//do this from server to see what server sees
            //event.player.addChatMessage(new ChatComponentText("Pain: " + status.getCurrentPain() + " Trauma: " + status.getCurrentTrauma() + " Adrenaline: " + status.getCurrentAdrenaline()));
    		if(status.getCurrentAdrenaline() < 50) {
    		if(status.getCurrentTrauma() < 6 && status.getCurrentTrauma() > 0) {
    			event.player.setJumping(false); //can never jump
    			event.player.motionX *= 0.35;
                event.player.motionZ *= 0.35;
    		}
    		else if(status.getCurrentTrauma() == 0) {
    			event.player.setJumping(false);
    			event.player.motionX *= 0.05;
                event.player.motionZ *= 0.05;
    		}
    		}
    		if(random.nextDouble() < 0.0625 && tick % 20 == 0 && status.getCurrentAdrenaline() > 85) { //only do on server
        	ItemStack stack = event.player.getHeldItem(); //send packet to client otherwise this doesn't work
        	if(stack != null) {
        	event.player.dropItem(stack.getItem(), stack.stackSize);
        	stack.stackSize = 0;
        	}
        }}
    }
}
