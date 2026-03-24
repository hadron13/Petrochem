package io.github.hadron13.petrochem.compat.adlods;

import com.endertech.minecraft.mods.adlods.AdLods;
import com.endertech.minecraft.mods.adlods.deposit.Deposit;
import com.endertech.minecraft.mods.adlods.ore.AbstractOre;
import com.endertech.minecraft.mods.adlods.target.TargetGenResult;
import com.endertech.minecraft.mods.adlods.world.WorldTargets;
import io.github.hadron13.petrochem.Petrochem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;



public class AdlodDepositDetector {

    public static Optional<Block> getDeposit(ServerLevel level, BlockPos position){

        if(!Petrochem.adlodsLoaded)
            return Optional.empty();

        List<TargetGenResult> deposits = WorldTargets.get(level).generated().values()
                .stream()
                .filter((dep) -> AbstractOre.withinRadius(new ChunkPos(dep.pos), new ChunkPos(position), 1))
                .sorted((a, b) -> (a.pos.distSqr(position) < b.pos.distSqr(position))? -1:1).toList();

        if(deposits.isEmpty()){
            return Optional.empty();
        }

        TargetGenResult closest = deposits.get(0);

        Optional<Deposit> matchingDeposit = AdLods.getInstance().features.depositGenerator.get().findByName(closest.name);

        if(matchingDeposit.isEmpty())
            return Optional.empty();


        return matchingDeposit.get().getPlacements().getOreBlocks().stream().findFirst();
    }
}
