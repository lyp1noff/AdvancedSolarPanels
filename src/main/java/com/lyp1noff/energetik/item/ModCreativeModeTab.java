package com.lyp1noff.energetik.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab ENERGETIK_TAB = new CreativeModeTab("energetikTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SILVER_INGOT.get());
        }
    };
}
