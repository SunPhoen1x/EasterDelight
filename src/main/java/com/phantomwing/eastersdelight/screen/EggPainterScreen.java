package com.phantomwing.eastersdelight.screen;

import com.phantomwing.eastersdelight.EastersDelight;
import com.phantomwing.eastersdelight.block.ModBlocks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;

import java.util.List;
import java.util.Optional;

public class EggPainterScreen extends ItemCombinerScreen<EggPainterMenu> {
    private static final ResourceLocation TEXTURE = getGUITexture(BuiltInRegistries.BLOCK.getKey(ModBlocks.EGG_PAINTER).getPath());
    private static final ResourceLocation EMPTY_SLOT_EGG = getIconTexture("empty_slot_egg");
    private static final ResourceLocation EMPTY_SLOT_COLOR = getIconTexture("empty_slot_color");
    private static final ResourceLocation EMPTY_SLOT_PATTERN = getIconTexture("empty_slot_pattern");

    private final CyclingSlotBackground eggIcon = new CyclingSlotBackground(EggPainterMenu.EGG_SLOT);
    private final CyclingSlotBackground baseColorIcon = new CyclingSlotBackground(EggPainterMenu.BASE_COLOR_SLOT);
    private final CyclingSlotBackground patternIcon = new CyclingSlotBackground(EggPainterMenu.PATTERN_SLOT);
    private final CyclingSlotBackground patternColorIcon = new CyclingSlotBackground(EggPainterMenu.PATTERN_COLOR_SLOT);

    private static final Component MISSING_EGG_TOOLTIP = Component.translatable(EastersDelight.MOD_ID + ".container.egg_painter.missing_egg_tooltip");
    private static final Component MISSING_BASE_COLOR_TOOLTIP = Component.translatable(EastersDelight.MOD_ID + ".container.egg_painter.missing_base_color_tooltip");
    private static final Component MISSING_PATTERN_TOOLTIP = Component.translatable(EastersDelight.MOD_ID + ".container.egg_painter.missing_pattern_tooltip");
    private static final Component MISSING_PATTERN_COLOR_TOOLTIP = Component.translatable(EastersDelight.MOD_ID + ".container.egg_painter.missing_pattern_color_tooltip");

    public EggPainterScreen(EggPainterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, TEXTURE);
    }

    @Override
    protected void renderErrorIcon(@NotNull GuiGraphics guiGraphics, int x, int y) {
        // We do not have an error state.
    }

    @Override
    public void containerTick() {
        super.containerTick();

        // Determine default icons when slots are empty.
        this.eggIcon.tick(List.of(EMPTY_SLOT_EGG));
        this.baseColorIcon.tick(List.of(EMPTY_SLOT_COLOR));
        this.patternIcon.tick(List.of(EMPTY_SLOT_PATTERN));
        this.patternColorIcon.tick(List.of(EMPTY_SLOT_COLOR));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltips(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        // Render default icons when slots are empty.
        this.eggIcon.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
        this.baseColorIcon.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
        this.patternIcon.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
        this.patternColorIcon.render(this.menu, guiGraphics, partialTick, this.leftPos, this.topPos);
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Optional<Component> optional = Optional.empty();

        if (this.hoveredSlot != null) {
            ItemStack hoveredItem = this.hoveredSlot.getItem();
            if (hoveredItem.isEmpty()) {
                if (this.hoveredSlot.index == EggPainterMenu.EGG_SLOT) {
                    optional = Optional.of(MISSING_EGG_TOOLTIP);
                }
                else if (this.hoveredSlot.index == EggPainterMenu.BASE_COLOR_SLOT) {
                    optional = Optional.of(MISSING_BASE_COLOR_TOOLTIP);
                }
                else if (this.hoveredSlot.index == EggPainterMenu.PATTERN_SLOT) {
                    optional = Optional.of(MISSING_PATTERN_TOOLTIP);
                }
                else if (this.hoveredSlot.index == EggPainterMenu.PATTERN_COLOR_SLOT) {
                    optional = Optional.of(MISSING_PATTERN_COLOR_TOOLTIP);
                }
            }
        }

        optional.ifPresent(component -> guiGraphics.renderTooltip(
                this.font,
                List.of(ClientTooltipComponent.create(component.getVisualOrderText())),
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                null
        ));
    }

    private static ResourceLocation getGUITexture(String textureName) {
        return ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID,
                "textures/gui/" + textureName + ".png"
        );
    }

    private static ResourceLocation getIconTexture(String textureName) {
        return ResourceLocation.fromNamespaceAndPath(EastersDelight.MOD_ID,
                "item/" + textureName
        );
    }
}
