package net.lightskin.moodles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerStatus implements IExtendedEntityProperties{
	public final static String EXT_PROP_NAME = "PlayerStatusBars";
	public static final int TRAUMA_WATCHER = 20;
	public static final int PAIN_WATCHER = 21;
	public static final int ADRENALINE_WATCHER = 22;
	private final EntityPlayer player;
	public PlayerStatus(EntityPlayer player1) {
		player = player1;
		player.getDataWatcher().addObject(TRAUMA_WATCHER, 10);
		player.getDataWatcher().addObject(PAIN_WATCHER, 0);
		player.getDataWatcher().addObject(ADRENALINE_WATCHER, 0);
	}
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(PlayerStatus.EXT_PROP_NAME, new PlayerStatus(player));
	}
	public static final PlayerStatus get(EntityPlayer player) {
		return (PlayerStatus) player.getExtendedProperties(EXT_PROP_NAME);
	}
	public void copy(PlayerStatus props) {
		player.getDataWatcher().updateObject(TRAUMA_WATCHER, props.getCurrentTrauma());
		player.getDataWatcher().updateObject(PAIN_WATCHER, props.getCurrentPain());
		player.getDataWatcher().updateObject(ADRENALINE_WATCHER, props.getCurrentAdrenaline());
	}
	@Override
	public void init(Entity arg0, World arg1) {}
	public void onUpdate() {
		// only want to update the timer and regen mana on the server:
		if (!player.worldObj.isRemote) {
			
		}
	}
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		//set stuff here
		properties.setInteger("traumabar", player.getDataWatcher().getWatchableObjectInt(TRAUMA_WATCHER));
		properties.setInteger("painbar", player.getDataWatcher().getWatchableObjectInt(PAIN_WATCHER));
		properties.setInteger("adrenalinebar", player.getDataWatcher().getWatchableObjectInt(ADRENALINE_WATCHER));
		compound.setTag(EXT_PROP_NAME, properties);
	}
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		player.getDataWatcher().updateObject(TRAUMA_WATCHER, properties.getInteger("traumabar"));
		player.getDataWatcher().updateObject(PAIN_WATCHER, properties.getInteger("painbar"));
		player.getDataWatcher().updateObject(ADRENALINE_WATCHER, properties.getInteger("adrenalinebar"));
	}
	public final int getCurrentTrauma() {
		return player.getDataWatcher().getWatchableObjectInt(TRAUMA_WATCHER);
	}
	public final int getCurrentPain() {
		return player.getDataWatcher().getWatchableObjectInt(PAIN_WATCHER);
	}
	public final int getCurrentAdrenaline() {
		return player.getDataWatcher().getWatchableObjectInt(ADRENALINE_WATCHER);
	}
	public void setTrauma(int amount) {
		player.getDataWatcher().updateObject(TRAUMA_WATCHER, amount > 0 ? (amount < 10 ? amount : 10) : 0);
	}
	public void setPain(int amount) {
		player.getDataWatcher().updateObject(PAIN_WATCHER, amount > 0 ? (amount < 20 ? amount : 20) : 0);
	}
	public void setAdrenaline(int amount) {
		player.getDataWatcher().updateObject(ADRENALINE_WATCHER, amount > 0 ? (amount < 100 ? amount : 100) : 0);
	}
}
