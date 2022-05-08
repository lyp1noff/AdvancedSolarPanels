package net.lyp1noff.energetik.block;

import net.lyp1noff.energetik.Energetik;
import net.lyp1noff.energetik.item.ModCreativeModeTab;
import net.lyp1noff.energetik.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Energetik.MOD_ID);

    public static final RegistryObject<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(3f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SILVER_ORE = registerBlock("silver_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(3f).requiresCorrectToolForDrops()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, ModCreativeModeTab.ENERGETIK_TAB);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(ModCreativeModeTab.ENERGETIK_TAB)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
