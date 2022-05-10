package net.lyp1noff.energetik.block.entity;

import net.lyp1noff.energetik.Energetik;
import net.lyp1noff.energetik.block.ModBlocks;
import net.lyp1noff.energetik.block.entity.custom.SolarPanelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Energetik.MOD_ID);

    public static final RegistryObject<BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("solar_panel", () ->
                    BlockEntityType.Builder.of(SolarPanelBlockEntity::new,
                            ModBlocks.SOLAR_PANEL.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}