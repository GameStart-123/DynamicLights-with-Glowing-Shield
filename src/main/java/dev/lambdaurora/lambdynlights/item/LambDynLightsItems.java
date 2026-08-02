/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the Lambda License. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdynlights.item;

import dev.lambdaurora.lambdynlights.LambDynLights;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.List;
import java.util.Optional;

/**
 * Registers LambDynamicLights items.
 */
public final class LambDynLightsItems implements ModInitializer {
	public static final Item GLOWING_SHIELD = register(
			"glowing_shield",
			key -> new ShieldItem(createShieldProperties().setId(key))
	);

	public LambDynLightsItems() {}

	@Override
	public void onInitialize() {
		initialize();
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("combat")))
				.register(output -> output.accept(GLOWING_SHIELD));
	}

	private static Item.Properties createShieldProperties() {
		return new Item.Properties()
				.durability(336)
				.component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
				.repairable(ItemTags.WOODEN_TOOL_MATERIALS)
				.equippableUnswappable(EquipmentSlot.OFFHAND)
				.delayedComponent(DataComponents.BLOCKS_ATTACKS, registryLookup -> new BlocksAttacks(
						0.25F,
						1.0F,
						List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
						new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
						Optional.of(registryLookup.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
						Optional.of(SoundEvents.SHIELD_BLOCK),
						Optional.of(SoundEvents.SHIELD_BREAK)
				))
				.component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK);
	}

	private static Item register(String path, ItemFactory factory) {
		Identifier id = LambDynLights.id(path);
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		return Registry.register(BuiltInRegistries.ITEM, key, factory.create(key));
	}

	@FunctionalInterface
	private interface ItemFactory {
		Item create(ResourceKey<Item> key);
	}
}
