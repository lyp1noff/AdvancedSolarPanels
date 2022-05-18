package net.lyp1noff.energetik;

import net.lyp1noff.energetik.block.ModBlocks;
import net.lyp1noff.energetik.block.entity.ModBlockEntities;
import net.lyp1noff.energetik.config.ModCommonConfigs;
import net.lyp1noff.energetik.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Energetik.MOD_ID)
public class Energetik
{
    public static final String MOD_ID = "energetik";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Energetik()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);

        ModBlockEntities.register(eventBus);

        eventBus.addListener(this::setup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC, "tutorialmod-common.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Welcome at Palace of Culture Energetik");
    }
}
