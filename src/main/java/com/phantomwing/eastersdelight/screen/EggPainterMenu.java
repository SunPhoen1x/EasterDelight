package com.phantomwing.eastersdelight.screen;

import com.phantomwing.eastersdelight.block.ModBlocks;
import com.phantomwing.eastersdelight.component.EggPattern;
import com.phantomwing.eastersdelight.component.ModDataComponents;
import com.phantomwing.eastersdelight.item.ModItems;
import com.phantomwing.eastersdelight.tags.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalInt;

public class EggPainterMenu extends ItemCombinerMenu {
    public static final int EGG_SLOT = 0;
    public static final int BASE_COLOR_SLOT = 1;
    public static final int PATTERN_SLOT = 2;
    public static final int PATTERN_COLOR_SLOT = 3;
    public static final int RESULT_SLOT = 4;

    private final Level level;

    public EggPainterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public EggPainterMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.EGG_PAINTER, containerId, playerInventory, access, createInputSlotDefinitions());
        this.level = playerInventory.player.level();
    }
    protected static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(EGG_SLOT, 49, 20, (item) -> item.is(ModTags.Items.PAINTABLE_EGGS))
                .withSlot(BASE_COLOR_SLOT, 31, 49, (item) -> item.getItem() instanceof DyeItem)
                .withSlot(PATTERN_SLOT, 49, 49, (item) -> item.is(ModItems.EGG_PATTERN))
                .withSlot(PATTERN_COLOR_SLOT, 67, 49, (item) -> item.getItem() instanceof DyeItem)
                .withResultSlot(RESULT_SLOT, 125, 49)
                .build();
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(ModBlocks.EGG_PAINTER);
    }

    @Override
    protected boolean mayPickup(@NotNull Player player, boolean hasStack) {
        return hasRequiredInputs();
    }

    @Override
    protected void onTake(@NotNull Player player, ItemStack stack) {
        stack.onCraftedBy(player, stack.getCount());
        this.resultSlots.awardUsedRecipes(player, this.getRelevantItems());

        this.shrinkStackInSlot(EGG_SLOT);
        this.shrinkStackInSlot(BASE_COLOR_SLOT);

        if (hasPatternInputs()) {
            this.shrinkStackInSlot(PATTERN_SLOT);
            this.shrinkStackInSlot(PATTERN_COLOR_SLOT);
        }

        this.access.execute((level, blockPos) -> {
            level.levelEvent(1044, blockPos, 0);
        });
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(
                this.inputSlots.getItem(EGG_SLOT),
                this.inputSlots.getItem(BASE_COLOR_SLOT),
                this.inputSlots.getItem(PATTERN_SLOT),
                this.inputSlots.getItem(PATTERN_COLOR_SLOT)
        );
    }

    private void shrinkStackInSlot(int index) {
        ItemStack itemstack = this.inputSlots.getItem(index);
        if (!itemstack.isEmpty()) {
            itemstack.shrink(1);
            this.inputSlots.setItem(index, itemstack);
        }
    }

    @Override
    public void createResult() {
        if (hasRequiredInputs()) {
            ItemStack itemstack = new ItemStack(ModItems.DYED_EGG);

            DyeItem baseDye = (DyeItem)this.inputSlots.getItem(BASE_COLOR_SLOT).getItem();
            itemstack.set(DataComponents.BASE_COLOR, baseDye.getDyeColor());

            if (hasPatternInputs()) {
                EggPattern eggPattern = this.inputSlots.getItem(PATTERN_SLOT).get(ModDataComponents.EGG_PATTERN);
                itemstack.set(ModDataComponents.EGG_PATTERN, eggPattern);

                DyeItem patternDye = (DyeItem)this.inputSlots.getItem(PATTERN_COLOR_SLOT).getItem();
                itemstack.set(ModDataComponents.PATTERN_COLOR, patternDye.getDyeColor());
            }

            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultSlots.setItem(0, itemstack);
            }
        } else {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    private boolean hasRequiredInputs() {
        return !this.inputSlots.getItem(EGG_SLOT).isEmpty()
                && !this.inputSlots.getItem(BASE_COLOR_SLOT).isEmpty();
    }

    private boolean hasPatternInputs() {
        return !this.inputSlots.getItem(PATTERN_SLOT).isEmpty()
                && !this.inputSlots.getItem(PATTERN_COLOR_SLOT).isEmpty()
                && !this.inputSlots.getItem(PATTERN_COLOR_SLOT).is(this.inputSlots.getItem(BASE_COLOR_SLOT).getItem());
    }

    public int getSlotToQuickMoveTo(@NotNull ItemStack stack) {
        return this.findSlotToQuickMoveTo(stack).orElse(EGG_SLOT);
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean canMoveIntoInputSlots(@NotNull ItemStack stack) {
        return this.findSlotToQuickMoveTo(stack).isPresent();
    }

    private OptionalInt findSlotToQuickMoveTo(ItemStack stack) {
        if (stack.is(ModTags.Items.PAINTABLE_EGGS)) {
            return OptionalInt.of(EGG_SLOT);
        } else if (stack.is(ModItems.EGG_PATTERN)) {
            return OptionalInt.of(PATTERN_SLOT);
        } else if (stack.getItem() instanceof DyeItem) {
            if (!this.getSlot(BASE_COLOR_SLOT).hasItem() || this.getSlot(BASE_COLOR_SLOT).getItem().is(stack.getItem())) {
                return OptionalInt.of(BASE_COLOR_SLOT);
            }
            return OptionalInt.of(PATTERN_COLOR_SLOT);
        }
        return OptionalInt.empty();
    }
}