package bluesillybeard.voidworld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.GameRules;

public class VoidWorld implements ModInitializer {
    public static final GameRules.Key<EnumRule<VoidWorldType>> VOID_WORLD_TYPE =
            GameRuleRegistry.register("voidWorldType", GameRules.Category.MISC, GameRuleFactory.createEnumRule(VoidWorldType.disabled));

    @Override
    public void onInitialize() {
        System.out.println("Void World Is Here!!!!!!");
    }
}
