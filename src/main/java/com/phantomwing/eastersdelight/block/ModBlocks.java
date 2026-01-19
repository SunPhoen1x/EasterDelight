package com.phantomwing.eastersdelight.block;

import com.phantomwing.eastersdelight.EastersDelight;
import com.phantomwing.eastersdelight.block.custom.EggPainterBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block EGG_PAINTER = registerBlock("egg_painter", Blocks.CRAFTING_TABLE,
            props -> new EggPainterBlock(props.noOcclusion()));

    private static Block registerBlock(String name, Block copy, Function<Block.Properties, Block> function) {
        return registerBlock(name, Block.Properties.ofFullCopy(copy), function);
    }

    private static Block registerBlock(String name, BlockBehaviour.Properties baseProps, Function<Block.Properties, Block> function) {
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID, name);
        Block.Properties finalProps = baseProps.setId(ResourceKey.create(Registries.BLOCK, loc));

        return Registry.register(BuiltInRegistries.BLOCK, loc, function.apply(finalProps));
    }

    public static void registerModBlocks() {
        EastersDelight.LOGGER.info("Registering blocks for " + EastersDelight.MOD_ID);
    }
}