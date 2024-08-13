package net.lightskin.moodles;


import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.lightskin.moodles.effect.MoodlesEffects;
import net.lightskin.moodles.entity.EntityHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO: decomp mazerooms and copy the potion code then 
//just use custom events and get textures etc
@Mod(modid = Moodles.MODID, version = Moodles.VERSION)
public class Moodles
{
    public static final String MODID = "moodles";
    public static final String VERSION = "1.0";
    
    public static final Logger logger = LogManager.getLogger(MODID);
    
    public static final FMLCommonHandler fmlHandler = FMLCommonHandler.instance();
    
    public static MoodlesEffects effects;
    
    //TODO: add balance and moodles purely for information (hunger, sanity, airquality, temperature, thirst, etc)
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	//item and block registering
    	effects = new MoodlesEffects();
    }
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
           //Tile, Entity, Gui registering
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        fmlHandler.bus().register(new PlayerHandler());
        MinecraftForge.EVENT_BUS.register(new EntityHandler());
        logger.info("TEST");
    }
    
}
