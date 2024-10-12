package io.grasspow.extrabotany.common.item.equipment.weapon;

import io.grasspow.extrabotany.common.entity.projectile.InfluxWaverProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.relic.RelicImpl;

public class InfluxWaverItem extends RelicSwordItem {

    public InfluxWaverItem(Properties prop) {
        super(Tiers.DIAMOND, 5, -2F, prop);
    }

    @Override
    public int getManaPerDamage() {
        return 500;
    }

    @Override
    public void attack(LivingEntity player, Entity target, int times, double speedTime, float damageTime) {
        Vec3 targetpos = target == null ? raytraceFromEntity(player, 64F, true).getLocation().add(0, 1, 0) : target.position().add(0, 1, 0);

        InfluxWaverProjectile proj = new InfluxWaverProjectile(player.level(), player, 1);
        proj.setPos(player.getX(), player.getY() + 1.1D, player.getZ());
        proj.setTargetPos(targetpos);
        proj.faceTargetAccurately(0.7F);
        proj.setDeltaMovement(proj.getDeltaMovement().scale(speedTime));
        proj.setStrikeTimes(3);
        player.level().addFreshEntity(proj);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }
}
