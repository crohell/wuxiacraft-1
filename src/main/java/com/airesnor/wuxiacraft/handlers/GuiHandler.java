package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.gui.CultivationGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

	public static final int CULTIVATION_GUI_ID = 0;

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch(ID) {
			case CULTIVATION_GUI_ID:
				return null;
		}
		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		switch (ID) {
			case CULTIVATION_GUI_ID:
				return new CultivationGui(player);
		}
		return null;
	}
}
