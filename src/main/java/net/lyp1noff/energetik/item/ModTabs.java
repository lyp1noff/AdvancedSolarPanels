package net.lyp1noff.energetik.item;

import net.lyp1noff.energetik.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTabs {
    public static final CreativeModeTab ENERGETIK_TAB = new CreativeModeTab("energetikTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.SOLAR_PANEL.get());
        }
    };
}
