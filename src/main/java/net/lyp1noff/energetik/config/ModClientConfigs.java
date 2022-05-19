package net.lyp1noff.energetik.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Configs for Energetik Mod");

        // HERE DEFINE CONFIGS

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
