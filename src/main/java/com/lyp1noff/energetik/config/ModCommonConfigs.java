package com.lyp1noff.energetik.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> SILVER_ORE_VEINS_PER_CHUNK;
    public static final ForgeConfigSpec.ConfigValue<Integer> SILVER_ORE_VEIN_SIZE;

    static {
        BUILDER.push("Configs for Energetik Mod");

        SILVER_ORE_VEINS_PER_CHUNK = BUILDER.define("Veins Per Chunk", 7);
        SILVER_ORE_VEIN_SIZE = BUILDER.defineInRange("Vein Size", 9, 4, 20);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
