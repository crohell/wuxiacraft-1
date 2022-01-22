package wuxiacraft.combat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.init.WuxiaElements;
import wuxiacraft.networking.CultivationSyncMessage;
import wuxiacraft.networking.WuxiaPacketHandler;
import wuxiacraft.util.MathUtil;

import java.util.Objects;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.FORGE)
public class CombatEventHandler {

	/**
	 * All damage done to players will be intersected and be applied through here!
	 * This will post a LivingDamageEvent for compatibility
	 * @param event a description of what is happening
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerHurt(LivingHurtEvent event) {
		if(!(event.getEntity() instanceof Player player)) return;
		var cultivation = Cultivation.get(player);
		WuxiaDamageSource source;
		//if not one of our damage sources
		if(!(event.getSource() instanceof WuxiaDamageSource)) {
			//then we convert it
			source = getElementalSourceFromVanillaSource(event.getSource());
		}
		else {
			source = (WuxiaDamageSource) event.getSource();
		}
		// TODO add elemental resistance
		ForgeHooks.onLivingDamage(event.getEntityLiving(), source, event.getAmount());
		event.setCanceled(true);
	}

	/**
	 * Converts a vanilla damage source of any type to elemental damage source
	 * @param source the vanilla source to be converted from
	 * @return the wuxia damage source with an element
	 */
	private static WuxiaDamageSource getElementalSourceFromVanillaSource(DamageSource source) {
		if (source.isFire()) return new WuxiaDamageSource(source.getMsgId(), WuxiaElements.FIRE.get());
		if (source == DamageSource.LIGHTNING_BOLT)
			return new WuxiaDamageSource(source.getMsgId(), WuxiaElements.LIGHTNING.get());
		if (source == DamageSource.FREEZE)
			return new WuxiaDamageSource(source.getMsgId(), WuxiaElements.WATER.get());
		return new WuxiaDamageSource(source.getMsgId(), WuxiaElements.PHYSICAL.get(), source.getEntity());
	}

	/**
	 * This will intersect all player damages
	 * This is here mostly for compatibility
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerDamage(LivingDamageEvent event) {
		if(!(event.getEntity() instanceof Player player)) return;
		var cultivation = Cultivation.get(player);
		cultivation.setHealth(cultivation.getHealth() - event.getAmount());

		event.getEntityLiving().getCombatTracker().recordDamage(event.getSource(), (float) cultivation.getHealth() + event.getAmount(), event.getAmount());
		player.awardStat(Stats.DAMAGE_TAKEN, (int) event.getAmount());
		//decided that food exhaustion has nothing to do with damage.
		//and it'll be used anyways to heal the character.

		if(cultivation.getHealth() <= 0) {
			player.setHealth(-1);
		}

		//this will make players health bar always keep up with
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntityLiving()), new CultivationSyncMessage(cultivation));
		event.setCanceled(true);
	}



	/**
	 * This will add the strength to basic attacks from the player
	 * This way i guess players won't have a ton of modifiers
	 * And i can also send mobs flying away
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onPlayerDealsDamage(LivingHurtEvent event) {
		if (!(event.getSource().getEntity() instanceof Player player)) return;
		if (event.getSource() instanceof WuxiaDamageSource)
			return; // Means it was wuxiacraft that came up with this attack, so damage is already calculated

		ICultivation cultivation = Cultivation.get(player);
		event.setAmount(event.getAmount() + (float) cultivation.getStrength());

		LivingEntity target = event.getEntityLiving();
		double maxHP = target.getMaxHealth();
		if(target instanceof Player targetPlayer) maxHP = Cultivation.get(targetPlayer).getMaxHealth();
		double knockSpeed = MathUtil.clamp((event.getAmount()*0.7- maxHP)*0.3, 0, 12);
		Vec3 diff = Objects.requireNonNull(event.getSource().getSourcePosition()).subtract(event.getEntityLiving().getPosition(0.5f));
		diff = diff.normalize();
		target.knockback((float) knockSpeed, diff.x, diff.z);
	}

}
