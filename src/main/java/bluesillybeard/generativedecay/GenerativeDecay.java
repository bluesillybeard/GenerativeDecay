package bluesillybeard.generativedecay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.GameRules;

public class GenerativeDecay implements ModInitializer {
    public static final GameRules.Key<EnumRule<DecayType>> DECAY_TYPE =
            GameRuleRegistry.register("decayType", GameRules.Category.MISC, GameRuleFactory.createEnumRule(DecayType.disabled));

    @Override
    public void onInitialize() {
        System.out.println("Void World Is Here!!!!!!");
    }
}
