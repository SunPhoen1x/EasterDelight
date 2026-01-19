package com.phantomwing.eastersdelight;

import com.phantomwing.eastersdelight.screen.EggPainterScreen;
import net.fabricmc.api.ClientModInitializer;
import com.phantomwing.eastersdelight.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class EastersDelightClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModMenuTypes.EGG_PAINTER, EggPainterScreen::new);
    }
}
