package com.phantomwing.eastersdelight.item;

import com.google.common.collect.Sets;
import com.phantomwing.eastersdelight.EastersDelight;
import com.phantomwing.eastersdelight.block.ModBlocks;
import com.phantomwing.eastersdelight.item.custom.DyedEggItem;
import com.phantomwing.eastersdelight.item.custom.EggPatternItem;
import com.phantomwing.eastersdelight.food.FoodValues;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.function.Function;

public class ModItems {
    public static final int EGG_STACK_SIZE = 16;
    public static final int BOWL_STACK_SIZE = 16;
    public static final int BOTTLE_STACK_SIZE = 16;

    public static LinkedHashSet<Item> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    public static final Item EGG_PAINTER = registerBlockWithTab(ModBlocks.EGG_PAINTER);

    public static final Item BOILED_EGG = registerWithTab("boiled_egg", foodItem(FoodValues.BOILED_EGG));
    public static final Item EGG_SLICE = registerWithTab("egg_slice", foodItem(FoodValues.EGG_SLICE, Consumables.DEFAULT_FOOD));

    public static final Item CHOCOLATE_EGG = registerWithTab("chocolate_egg", foodItem(FoodValues.CHOCOLATE_EGG));
    public static final Item BUNNY_COOKIE = registerWithTab("bunny_cookie", foodItem(vectorwing.farmersdelight.common.FoodValues.COOKIES));

    public static final Item EGG_PATTERN = registerWithTab("egg_pattern", EggPatternItem::new, baseItem());

    public static final Item DYED_EGG = registerWithTab("dyed_egg", DyedEggItem::new, foodItem(FoodValues.BOILED_EGG));

    public static Item.Properties baseItem() {
        return new Item.Properties();
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return foodItem(food, null);
    }

    public static Item.Properties foodItem(FoodProperties food, @Nullable Consumable consumable) {
        return baseItem()
                .food(food)
                .component(DataComponents.CONSUMABLE, consumable != null ? consumable : Consumables.DEFAULT_FOOD);
    }

    public static Item.Properties bottleItem(@Nullable FoodProperties food, @Nullable Consumable consumable) {
        Item.Properties props = baseItem().craftRemainder(Items.GLASS_BOTTLE).stacksTo(BOTTLE_STACK_SIZE)
                .component(DataComponents.CONSUMABLE, consumable != null ? consumable : Consumables.DEFAULT_DRINK);
        if (food != null) props.food(food);
        return props;
    }

    public static Item.Properties bowlItem() {
        return baseItem().craftRemainder(Items.BOWL).stacksTo(BOWL_STACK_SIZE);
    }

    public static Item.Properties feastItem() {
        return baseItem().craftRemainder(Items.BOWL).stacksTo(1);
    }

    private static Item registerWithTab(String name, Item.Properties props) {
        return registerWithTab(name, Item::new, props);
    }

    private static Item registerWithTab(String name, Function<Item.Properties, Item> function, Item.Properties props) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, name);
        props.setId(ResourceKey.create(Registries.ITEM, loc));

        Item item = function.apply(props);
        CREATIVE_TAB_ITEMS.add(item);
        return Registry.register(BuiltInRegistries.ITEM, loc, item);
    }

    private static Item registerBlockWithTab(Block block) {
        return registerBlockWithTab(block, baseItem());
    }

    private static Item registerBlockWithTab(Block block, Item.Properties props) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, name);

        props.useBlockDescriptionPrefix();
        props.setId(ResourceKey.create(Registries.ITEM, loc));

        BlockItem item = new BlockItem(block, props);
        CREATIVE_TAB_ITEMS.add(item);
        return Registry.register(BuiltInRegistries.ITEM, loc, item);
    }

    public static void registerModItems() {
        EastersDelight.LOGGER.info("Registering items for " + EastersDelight.MOD_ID);
    }
}