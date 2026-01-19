package com.phantomwing.eastersdelight.villager;

import com.google.common.collect.ImmutableSet;
import com.phantomwing.eastersdelight.EastersDelight;
import com.phantomwing.eastersdelight.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;

public class ModVillagers {
    public static final ResourceKey<PoiType> EGG_BUNNY_POI_KEY = registerPoiKey("egg_bunny_poi");
    public static final PoiType EGG_BUNNY_POI = registerPOI("egg_bunny_poi", ModBlocks.EGG_PAINTER);

    public static final VillagerProfession EGG_BUNNY_PROFESSION = registerProfession("egg_bunny", EGG_BUNNY_POI_KEY);

    private static VillagerProfession registerProfession(String name, ResourceKey<PoiType> type) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, name);

        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, id,
                new VillagerProfession(
                        Component.translatable("entity.minecraft.villager." + EastersDelight.MOD_ID + "." + name),
                        entry -> entry.is(type),
                        entry -> entry.is(type),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.VILLAGER_WORK_CARTOGRAPHER));
    }

    private static PoiType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, name),
                1, 1, block);
    }

    private static ResourceKey<PoiType> registerPoiKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, name));
    }

    public static void register() {
        EastersDelight.LOGGER.info("Registering villager professions for " + EastersDelight.MOD_ID);
    }
}