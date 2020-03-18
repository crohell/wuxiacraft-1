package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.Blocks;
import com.airesnor.wuxiacraft.entities.mobs.GiantAnt;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.Items;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		WuxiaCraft.logger.info("Registering items.");
		event.getRegistry().registerAll(Items.ITEMS.toArray(new Item[0]));

	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		WuxiaCraft.logger.info("Registering blocks.");
		event.getRegistry().registerAll(Blocks.BLOCKS.toArray(new Block[0]));

		GameRegistry.registerTileEntity(CauldronTileEntity.class, new ResourceLocation("wuxiacraft", "cauldron_tile_entity"));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : Items.ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModels();
			}
		}
		for (Block block : Blocks.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			}
		}
	}

	@SubscribeEvent
	public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
		EntityEntry entity = EntityEntryBuilder.create()
				.entity(GiantAnt.class)
				.id(new ResourceLocation(WuxiaCraft.MODID, "giant_ant"),0)
				.name("giant_ant")
				.tracker(80, 3, false)
				.egg(0xFACB27, 0x202020)
				.build();
		event.getRegistry().register(entity);
	}

}
