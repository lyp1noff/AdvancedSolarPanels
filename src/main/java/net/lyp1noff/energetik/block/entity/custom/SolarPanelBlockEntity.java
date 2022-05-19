package net.lyp1noff.energetik.block.entity.custom;

import net.lyp1noff.energetik.block.entity.ModBlockEntities;
import net.lyp1noff.energetik.block.util.EnergetikEnergyStorage;
import net.lyp1noff.energetik.screen.SolarPanelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;


public class SolarPanelBlockEntity extends BlockEntity implements MenuProvider {
    private final int capacity = 1000;
    private final int baseEnergyProduction = 100;
    private final int maxExtract = baseEnergyProduction;
    public final EnergetikEnergyStorage energyStorage;
    private LazyOptional<EnergetikEnergyStorage> lazyEnergy;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ContainerData data;

    public SolarPanelBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SOLAR_PANEL_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        this.energyStorage = createEnergyStorage();
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> SolarPanelBlockEntity.this.getLazyEnergy();
                    case 1 -> SolarPanelBlockEntity.this.capacity;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                if (index == 0) {
                    SolarPanelBlockEntity.this.energyStorage.setEnergy(value);
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }
    private EnergetikEnergyStorage createEnergyStorage() {
        return new EnergetikEnergyStorage(this, this.capacity, 0, this.maxExtract, 0);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.lazyEnergy = LazyOptional.of(() -> this.energyStorage);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.lazyEnergy.cast();
        }
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public int getLazyEnergy() {
        return this.energyStorage.getEnergyStored();
    }

    private void produceSolarEnergy() {
        int energyProduction = 0;
        float dayTime = level.getDayTime();
        int skyBrightness = level.getBrightness(LightLayer.SKY, this.worldPosition.above());

        Block upperBlock = level.getBlockState(this.worldPosition.above()).getBlock();
//        if (upperBlock != Blocks.AIR) return;

        if (skyBrightness < 15) return;
        if (level.isThundering() || level.isRaining()) return;

        if (dayTime > 0 && dayTime < 1000) {
            energyProduction = (int) ((float) baseEnergyProduction / 1000 * dayTime);
        } else if (dayTime >= 1000 && dayTime < 11000) {
            energyProduction = baseEnergyProduction;
        } else if (dayTime >= 11000 && dayTime < 12000) {
            energyProduction = (int) (baseEnergyProduction - ((dayTime - 11000) / 1000 * baseEnergyProduction));
        }
        energyStorage.setEnergy(energyStorage.getEnergyStored() + energyProduction);
    }

    private void outputEnergy() {
        if (this.energyStorage.getEnergyStored() >= this.maxExtract && this.energyStorage.canExtract()) {
            for (final var direction : Direction.values()) {
                if (direction == Direction.UP) continue;
                final BlockEntity neighbour = this.level.getBlockEntity(this.worldPosition.relative(direction));
                if (neighbour == null) {
                    continue;
                }
                neighbour.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                    if (neighbour != this && storage.getEnergyStored() < storage.getMaxEnergyStored()) {
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

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyEnergy.invalidate();
        lazyItemHandler.invalidate();
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        produceSolarEnergy();
        outputEnergy();
        setChanged(pLevel, pPos, pState);
    }

    public void update() {
        requestModelDataUpdate();
        setChanged();
        if (this.level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("energy", getLazyEnergy());
        tag.put("inventory", itemHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.energyStorage.setEnergy(tag.getInt("energy"));
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Solar Panel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new SolarPanelMenu(pContainerId, pInventory, this, this.data);
    }
}
