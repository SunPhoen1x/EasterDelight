package com.phantomwing.eastersdelight.villager;

import com.phantomwing.eastersdelight.EastersDelight;
import com.phantomwing.eastersdelight.component.EggPattern;
import com.phantomwing.eastersdelight.component.ModDataComponents;
import com.phantomwing.eastersdelight.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModVillagerTrades {
    public static float EMERALD_MULTIPLIER = 0.2f;
    private static final ResourceKey<VillagerProfession> EGG_BUNNY_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION,
                    ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, "egg_bunny"));

    public static void register() {
        // 1: Novice
        addPatternTrades(1, 2, EggPattern.STRIPES, EggPattern.STRIPES_2, EggPattern.STRIPES_3);
        // 2: Apprentice
        addPatternTrades(2, 5, EggPattern.DIPPED, EggPattern.SPLIT, EggPattern.BLOCKS);
        // 3: Journeyman
        addPatternTrades(3, 10, EggPattern.PETALS, EggPattern.WAVES);
        // 4: Expert
        addPatternTrades(4, 15, EggPattern.HEART, EggPattern.DOTS);
        // 5: Master
        addPatternTrades(5, 30, EggPattern.CREEPER);
    }

    private static void addPatternTrades(int level, int xp, EggPattern... patterns) {
        TradeOfferHelper.registerVillagerOffers(EGG_BUNNY_KEY, level,
                factories -> {
                    for (EggPattern pattern : patterns) {
                        factories.add((entity, random) -> new MerchantOffer(
                                new ItemCost(Items.EMERALD, 1),
                                getPatternItem(pattern, 8),
                                16, xp, EMERALD_MULTIPLIER));
                    }

                    factories.add((entity, random) -> new MerchantOffer(
                            new ItemCost(Items.EMERALD, 2),
                            getRandomEasterEggItem(random, 4, patterns),
                            16, xp, EMERALD_MULTIPLIER));
                }
        );
    }

    private static ItemStack getPatternItem(EggPattern pattern, int count) {
        ItemStack patternStack = new ItemStack(ModItems.EGG_PATTERN, count);
        patternStack.set(ModDataComponents.EGG_PATTERN, pattern);
        return patternStack;
    }

    private static ItemStack getRandomEasterEggItem(RandomSource random, int count, EggPattern... patterns) {
        EggPattern pattern = patterns[random.nextInt(patterns.length)];
        DyeColor[] colors = DyeColor.values();
        DyeColor baseColor = colors[random.nextInt(colors.length)];

        List<DyeColor> filteredColors = Arrays.stream(colors).filter((color) -> color != baseColor).toList();
        DyeColor patternColor = filteredColors.get(random.nextInt(filteredColors.size()));

        ItemStack eggStack = new ItemStack(ModItems.DYED_EGG, count);
        eggStack.set(DataComponents.BASE_COLOR, baseColor);
        eggStack.set(ModDataComponents.EGG_PATTERN, pattern);
        eggStack.set(ModDataComponents.PATTERN_COLOR, patternColor);

        return eggStack;
    }
}