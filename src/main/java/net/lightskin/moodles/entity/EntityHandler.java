package net.lightskin.moodles.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.lightskin.moodles.Moodles;
import net.lightskin.moodles.PlayerStatus;
import net.lightskin.moodles.effect.MoodleEffect;
import net.lightskin.moodles.effect.MoodlesEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
//TODO: find motivation or seomething
//hyperaware at night if not sleepy.
//use insane status
public class EntityHandler {
	Random random = new Random();
	private class MoodleInstance{
		public MoodleEffect moodle = null;
		public PotionEffect effect = null;
		public int framesTillOld = 30; //ticks until its in position
		boolean removal = false;
		public MoodleInstance(MoodleEffect moodlein, PotionEffect effectin) {
			moodle = moodlein;
			effect = effectin;
		}
	}
	 ArrayList<MoodleInstance> importantMoodles = new ArrayList<MoodleInstance>();
	 ArrayList<MoodleInstance> nonImportantMoodles = new ArrayList<MoodleInstance>();
	 //add realistic pain
	 //wow this code is actually god-awful
	//okay I admit this part has nothing to do with 'entity' and everything to do with player but I"m not making a seprate class just for this
	 @SideOnly(Side.CLIENT)
	 @SubscribeEvent(priority=EventPriority.NORMAL)
	 public void onHudRender(RenderGameOverlayEvent event) {
		 switch(event.type) {
		 case HEALTH://todo: figure out why this was rendering in the modpack
		 case AIR:
		 case HEALTHMOUNT:
		 case FOOD:
		 case ARMOR:
		 case JUMPBAR:
		 case CROSSHAIRS:
		 case EXPERIENCE:
		 case BOSSHEALTH:
			 event.setCanceled(true);
			 break;
		 case HOTBAR:
			 Minecraft mc = Minecraft.getMinecraft();
			 EntityPlayer player = mc.thePlayer;
			 if(player.capabilities.isCreativeMode)
				 break;
			 PlayerStatus status = PlayerStatus.get(player);
			 Iterator a = player.getActivePotionEffects().iterator();
			 ArrayList<Integer> ids = new ArrayList<Integer>();
			 while(a.hasNext()) {
				 PotionEffect effect = (PotionEffect) a.next();
				 MoodleEffect moodle = null;
				 if(MoodlesEffects.easyAccess.containsKey(effect.getPotionID()))
					 moodle = MoodlesEffects.easyAccess.get(effect.getPotionID());
				 ids.add(moodle.id);
				 Iterator[] its = {importantMoodles.iterator(), nonImportantMoodles.iterator()};
				 boolean perform = true;
				 while(its[(moodle.important ? 0 : 1)].hasNext()) {
					 MoodleInstance ins = (MoodleInstance)its[(moodle.important ? 0 : 1)].next();
					 if(moodle == ins.moodle)
						 perform = false;		   
					 if(effect.getAmplifier() != ins.effect.getAmplifier())
						 ins.removal = true;	 
					 }
					 if(perform)
						 (moodle.important ? importantMoodles : nonImportantMoodles).add(new MoodleInstance(moodle, effect));
				 
		     }
			 float health = player.getHealth();
			 String textStatus = health < 20 ? (health < 14 ? (health < 10 ? (health < 6 ? (health == 1 ? "Death at The Gates" : "Grave") : "Very Unwell") : "Injured") : "Harmed.") : "Fine.";
			 int posx = event.resolution.getScaledWidth() / 3 - 15, posy = event.resolution.getScaledHeight() - 45;
			 if(importantMoodles.size() + nonImportantMoodles.size() == 0)
				 mc.fontRenderer.drawString(textStatus, posx, posy, 0xFFFFFF, true);
			 else { //TODO: why is broken bones and agony acting up?
				 mc.fontRenderer.drawString(textStatus, posx, posy - 19, 0xFFFF00, true);
				 MoodlesEffects.drawMoodleContainer(posx - 10,posy,posx * 2,19, 10);
				 Iterator[] its = {importantMoodles.iterator(), nonImportantMoodles.iterator()};
				 MoodleInstance tmp = null, remove = null;
				 int index = 0, removeI = -1;
				 for(int i = 0; i < 2; i++)
				 while(its[i].hasNext()) {		
					 tmp = (MoodleInstance) its[i].next();
					 if(!ids.contains(tmp.effect.getPotionID()))
						 tmp.removal = true;
					 if(tmp.framesTillOld > 30) {
						 remove = tmp;
						 removeI = i;
						 continue;
					 }
					 MoodlesEffects.renderInventoryEffect(posx + index * (i == 0 ? 1 : -1) * 19, (int)((float)posy + 40.0 * ((float)tmp.framesTillOld / 30.0)), tmp.effect, mc, tmp.moodle.icons);
					 //player.addChatComponentMessage(new ChatComponentText("frames" + tmp.framesTillOld + " removal" + tmp.removal));
					 if(tmp.framesTillOld > 0 && !tmp.removal)
						 tmp.framesTillOld--;
					 else if(tmp.removal)
						 tmp.framesTillOld++;
					 index++;
				 }
				 if(remove != null)
					 (removeI == 0 ? importantMoodles : nonImportantMoodles).remove(remove);
			 }
			 
			 break;
		 }
	 }
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			if (PlayerStatus.get((EntityPlayer) event.entity) == null) {
				PlayerStatus.register((EntityPlayer) event.entity);
			}
		}
	}
    
    @SubscribeEvent
    public void AttackEntity(AttackEntityEvent event){
    	PlayerStatus status = PlayerStatus.get(event.entityPlayer);
    	if(random.nextDouble() < status.getCurrentPain() / 20.0 && status.getCurrentAdrenaline() < 50) {
    		event.setCanceled(true);
    		event.entityPlayer.addChatMessage(new ChatComponentText("Miss")); //should probably make a noise here
    	}
    }
    
	@SubscribeEvent
	public void ClonePlayer(PlayerEvent.Clone event) {
		PlayerStatus.get(event.entityPlayer).copy(PlayerStatus.get(event.original));
	}
    
    @SubscribeEvent
    public void EntityHurt(LivingHurtEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if(entity instanceof EntityPlayer) {
        	EntityPlayer player = (EntityPlayer)entity;
        	PlayerStatus status = PlayerStatus.get(player);
        	//do trauma/pain calculations here
        	if(event.source.damageType != "drown" && event.source.damageType != "magic"){
        		status.setPain(status.getCurrentPain() + (int)Float.max(1.0f,event.ammount));
        	}
        	if(event.source.damageType == "fall") {
        		int newTrauma = status.getCurrentTrauma() - (int)Float.max(1.0f,event.ammount);
        		if(newTrauma < 0)
        			event.ammount = Math.abs(newTrauma);
        		else
        			event.setCanceled(true);
        		int pastTrauma = status.getCurrentTrauma();
        		status.setTrauma(newTrauma);
        		if(pastTrauma - status.getCurrentTrauma() == 10)
        			status.setAdrenaline(status.getCurrentAdrenaline() + 51);
        	}
        }
        else if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer){
        	PlayerStatus status = PlayerStatus.get((EntityPlayer)event.source.getEntity());
        	if(random.nextDouble() > status.getCurrentPain() / 20.0)
        		event.ammount = 0;
            ((EntityPlayer)event.source.getEntity()).addChatMessage(new ChatComponentText("In here!"));
        }
        return; //add code for entities?
    }
    @SubscribeEvent
    public void EntityDie(LivingDeathEvent event) {
       if(event.entity instanceof EntityPlayer) {
    	   PlayerStatus status = PlayerStatus.get((EntityPlayer)event.entity);
    	   status.setPain(0);
    	   status.setTrauma(10);
    	   status.setAdrenaline(0);
       }
    }
}
