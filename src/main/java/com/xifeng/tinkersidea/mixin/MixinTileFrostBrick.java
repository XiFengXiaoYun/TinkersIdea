package com.xifeng.tinkersidea.mixin;
/*
import com.dungeon_additions.da.entity.tileEntity.TileEntityFrostBrick;
import com.dungeon_additions.da.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

@Mixin(value = TileEntityFrostBrick.class)
public class MixinTileFrostBrick extends TileEntity {

    @Shadow(remap = false)
    private int age;


    @Overwrite(remap = false)
    public void func_73660_a() {
        World world = this.getWorld();
        if(world.isRemote || world.getTotalWorldTime() % 20 != 0) {
            return;
        }
        IBlockState stateUp = world.getBlockState(this.getPos().up());
        if(stateUp.getBlock() != Blocks.ICE) return;

        if(!world.isAirBlock(this.getPos().down())) return;

        if(world.rand.nextInt(15) == 0) {
            age++;
            if(age >= 10) {
                world.setBlockState(this.getPos().down(), ModBlocks.ICICLE_BLOCK.getDefaultState());
            }
        }
    }
}
*/
