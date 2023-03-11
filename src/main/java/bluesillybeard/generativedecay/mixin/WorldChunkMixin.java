package bluesillybeard.generativedecay.mixin;

import bluesillybeard.generativedecay.GenerativeDecay;
import bluesillybeard.generativedecay.DecayType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
    private static final Block[] EXCEPTIONS = {
            Blocks.END_PORTAL_FRAME, //end portal so that people can still beat the game
            Blocks.SPAWNER //This just makes it easier, since spawners were so rare to begin with.
    };

    @Inject(method = "<init>(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/ProtoChunk;Lnet/minecraft/world/chunk/WorldChunk$EntityLoader;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/chunk/ProtoChunk;getBlockEntities()Ljava/util/Map;"))
    private void WorldChunkInitInject(ServerWorld world, ProtoChunk protoChunk, WorldChunk.EntityLoader entityLoader, CallbackInfo ci){
        //we remove the blocks when the chunk is finalized to become part of the world.
        // Side note, what the actual flip is this? no wonder minecraft is so badly optimized!
        // they literally MAKE AN ENTIRE COPY OF THE CHUNK AND DELETE THE OLD ONE
        // for LITERALLY Every Single Chunk.
        //Worldgen is also just a giant spaghetti mess to begin with lol

        //This constructor is only called when converting a ProtoChunk (a chunk in the process of being generated)
        // into a WorldChunk (a chunk that is a part of the world), so loading chunks don't get re-voided.

        makeVoid(protoChunk, world.getGameRules().get(GenerativeDecay.DECAY_TYPE).get()); //What a chain of functions lol
    }

    /**
     * Applies a decay modification to a chunk,
     * @param chunk the chunk to remove the blocks of.
     */
    private void makeVoid(Chunk chunk, DecayType type){
        //The random is based on the chunk's position.
        Random random = new Random(chunk.getPos().hashCode());
        if(type == DecayType.disabled)return; //if it's disabled we can just move on.
        BlockPos start = chunk.getPos().getStartPos();
        int bottomY = chunk.getBottomY();
        int topY = chunk.getHighestNonEmptySectionYOffset()+16; //bad things happen without the +16.
        BlockState air = Blocks.AIR.getDefaultState();
        for(int x=0; x<16; ++x){
            for(int y=bottomY; y<topY; ++y){
                for(int z=0; z<16; ++z){
                    BlockPos pos = start.add(x, y, z);
                    if(
                        shouldRemove(pos.getX(), pos.getY(), pos.getZ(), type, random)
                        && isNotException(chunk.getBlockState(pos))
                    )
                    {
                        chunk.setBlockState(pos, air, false);
                        chunk.removeBlockEntity(pos);
                    }
                }
            }
        }
    }

    private static boolean shouldRemove(int x, int y, int z, DecayType type, Random rand){
        return switch(type){
            case disabled -> false; //no blocks removed
            case distance1 -> !(((x&1) == 0) && ((y&1)==0) && ((z&1)==0)); //every other block
            case distance2 -> !(((x%3) == 0) && ((y%3)==0) && ((z%3)==0)); //every 3 blocks
            case distance3 -> !(((x&3) == 0) && ((y&3)==0) && ((z&3)==0)); //every 4 blocks
            case random25 -> rand.nextDouble() < 0.25; //25 percent chance
            case random50 -> rand.nextDouble() < 0.5; //50 percent chance
            case random75 -> rand.nextDouble() < 0.75; //75 percent chance
            case random90 -> rand.nextDouble() < 0.9; //90 percent chance
            case random99 -> rand.nextDouble() < 0.99; //99 percent chance
            //case completeVoid -> true; //always remove, except the block at 0,60, 0 so there is a place to spawn
            //default -> false;
        };
    }

    /**
     * Determines if a given block is one of the exceptions
     * @param state The block you want to know if it's an exception or not
     * @return a boolean, false if the block is an exception, true if it isn't an exception.
     */
    private static boolean isNotException(BlockState state){
        for(Block block : EXCEPTIONS){
            if(state.isOf(block))return false;
        }
        return true;
    }

    @Redirect(method="runPostProcessing", at = @At(value = "INVOKE", target="Lnet/minecraft/fluid/FluidState;onScheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    private void runPostProcessingRedirect(FluidState instance, World world, BlockPos pos){
        //here we stop fluid ticks from happening as a chunk is loaded/converted.

        //As far as I can tell, this was mainly used to stop water from being still after worldgen and loading the world.
        // it only works sometimes for a void world (probably since it's not designed for the insanity that is a void world)
        // Though often it doesn't even work in the vanilla game sometimes (lol)

        //this just makes it to where water is left floating, to keep the "gridworld" look even with water in the mix.
    }

}