package io.grasspow.extrabotany.common.item.equipment.bauble;

import io.grasspow.extrabotany.common.event.DamageEventHandler;
import io.grasspow.extrabotany.common.handler.DamageHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.phys.AABB;
import vazkii.botania.api.mana.ManaItemHandler;

public class FrostStarItem extends BaubleItem {

    public FrostStarItem(Properties props) {
        super(props);
    }

    private static final int RANGE = 6;
    private static final int MANA_PER_DAMAGE = 30;

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity1) {
        super.onWornTick(stack, entity1);
        if (entity1 instanceof Player player) {
            if (!player.level().isClientSide()) {
                boolean lastOnGround = player.onGround();
                player.setOnGround(true);
                FrostWalkerEnchantment.onEntityMoved(player, player.level(), player.getOnPos(), 8);
                player.setOnGround(lastOnGround);

                for (LivingEntity living : player.level().getEntitiesOfClass(LivingEntity.class, new AABB(player.getOnPos().offset(-RANGE, -RANGE, -RANGE), player.getOnPos().offset(RANGE + 1, RANGE + 1, RANGE + 1)))) {
                    if (((ServerPlayer) player).getCamera() != living
                            && living != player
                            && DamageHandler.checkPassable(living, player)
                            && player.tickCount % 20 == 0
                            && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_PER_DAMAGE, true)) {
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 4));
                    }
                }
            }
        }
    }
}
