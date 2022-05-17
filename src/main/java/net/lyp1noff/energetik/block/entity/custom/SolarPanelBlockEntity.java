package net.lyp1noff.energetik.block.entity.custom;

import net.lyp1noff.energetik.block.entity.ModBlockEntities;
import net.lyp1noff.energetik.block.util.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;

public class SolarPanelBlockEntity extends BlockEntity {

    public final CustomEnergyStorage energyStorage;
    private final int capacity = 1000;
    private final int baseEnergyProduction = 100;
    private final int maxExtract = baseEnergyProduction;
    private final LazyOptional<CustomEnergyStorage> energy;

    public SolarPanelBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SOLAR_PANEL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        this.energyStorage = createEnergyStorage();
        this.energy = LazyOptional.of(() -> this.energyStorage);
    }
    private CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(this, this.capacity, 0, this.maxExtract, 0);
    }
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.energyStorage.setEnergy(tag.getInt("Energy"));
    }

    public int getEnergy() {
        return this.energyStorage.getEnergyStored();
    }

    public void outputEnergy() {
        if (this.energyStorage.getEnergyStored() >= this.maxExtract && this.energyStorage.canExtract()) {
            for (final var direction : Direction.values()) {
                final BlockEntity be = this.level.getBlockEntity(this.worldPosition.relative(direction));
                if (be == null) {
                    continue;
                }

                be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                    if (be != this && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                        final int toSend = SolarPanelBlockEntity.this.energyStorage.extractEnergy(this.maxExtract,
                                false);
                        final int received = storage.receiveEnergy(toSend, false);
                        SolarPanelBlockEntity.this.energyStorage.setEnergy(
                                SolarPanelBlockEntity.this.energyStorage.getEnergyStored() + toSend - received);
                    }
                });
            }
        }
    }

    public void tick() {
        outputEnergy();
        float dayTime = level.getDayTime();
        int energyProduction = 0;
        if (dayTime > 0 && dayTime < 1000) {
            energyProduction = (int) ((float) baseEnergyProduction / 1000 * dayTime);
        } else if (dayTime >= 1000 && dayTime < 11000) {
            energyProduction = baseEnergyProduction;
        } else if (dayTime >= 11000 && dayTime < 12000) {
            energyProduction = (int) (baseEnergyProduction - ((dayTime - 11000) / 1000 * baseEnergyProduction));
        }

        energyStorage.setEnergy(energyStorage.getEnergyStored() + energyProduction);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Energy", getEnergy());
    }

    public void update() {
        requestModelDataUpdate();
        setChanged();
        if (this.level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
        }
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return cap == CapabilityEnergy.ENERGY ? this.energy.cast() : super.getCapability(cap, side);
    }
}
