package net.lightskin.moodles.effect;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class MoodleEffect extends Potion {
    protected MoodleEffect(int arg0, boolean arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}
	public Map<Integer,ResourceLocation> icons = Maps.newHashMap();
    public Map<Integer, String[]> moodleFields = Maps.newHashMap();
    public boolean important = true, standard = true;
}
