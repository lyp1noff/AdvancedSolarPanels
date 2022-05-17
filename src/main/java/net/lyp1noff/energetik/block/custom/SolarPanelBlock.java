package net.lyp1noff.energetik.block.custom;

import net.lyp1noff.energetik.block.entity.ModBlockEntities;
import net.lyp1noff.energetik.block.entity.custom.SolarPanelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SolarPanelBlock extends BaseEntityBlock {
    public SolarPanelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        return createTickerHelper(pBlockEntityType, ModBlockEntities.SOLAR_PANEL_BLOCK_ENTITY.get(),
//                SolarPanelBlockEntity.tick(pLevel, pState));

        return pLevel.isClientSide ? null
                    : (level0, pos, state0, blockEntity) -> ((SolarPanelBlockEntity) blockEntity).tick();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SolarPanelBlockEntity(pos, state);
    }
}