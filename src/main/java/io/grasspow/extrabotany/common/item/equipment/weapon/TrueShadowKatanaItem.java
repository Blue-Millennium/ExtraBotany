package io.grasspow.extrabotany.common.item.equipment.weapon;

import io.grasspow.extrabotany.common.entity.projectile.TrueShadowKatanaProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.item.relic.RelicImpl;

import java.util.List;

import static io.grasspow.extrabotany.common.libs.CommonHelper.getFilteredEntities;

public class TrueShadowKatanaItem extends RelicSwordItem {
    public TrueShadowKatanaItem(Item.Properties prop) {
        super(Tiers.DIAMOND, 5, -2F, prop);
    }

    public void attack(LivingEntity player, Entity target, int times, double speedTime, float damageTime) {
        Vec3 targetpos = Vec3.ZERO;

        float RANGE = 8F;
        AABB axis_ = new AABB(player.position().add(-RANGE, -RANGE, -RANGE)
                , player.position().add(RANGE + 1, RANGE + 1, RANGE + 1));

        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, axis_);
        List<LivingEntity> list = getFilteredEntities(entities, player);

        if (list.size() == 0) {
            targetpos = target == null ? raytraceFromEntity(player, 64F, true).getLocation().add(0, 1, 0) : target.position().add(0, 1, 0);
        } else if (player instanceof Mob && ((Mob) player).getTarget() != null && entities.contains(((Mob) player).getTarget())) {
            targetpos = ((Mob) player).getTarget().position();
        } else if (player.getLastHurtMob() != null && entities.contains(player.getLastHurtMob())) {
            targetpos = player.getLastHurtMob().position();
        } else {
            for (LivingEntity living : entities) {
                if (living != player) {
                    targetpos = living.position();
                    break;
                }
            }
        }
        for (int i = 3 - times; i < 3; i++) {
            Vec3 look = new Vec3(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z).multiply(1, 0, 1);

            double playerRot = Math.toRadians(player.yRotO + 90);
            if (look.x == 0 && look.z == 0) {
                look = new Vec3(Math.cos(playerRot), 0, Math.sin(playerRot));
            }

            look = look.normalize().scale(1.75);

            int div = i / 3;
            int mod = i % 3;

            Vec3 pl = look.add(VecHelper.fromEntityCenter(player)).add(0, 0.1, div * 0.1);

            Vec3 axis = look.normalize().cross(new Vec3(-1, 0, -1)).normalize();

            double rot = mod * Math.PI / 4 - Math.PI / 4;
            //VecHelper.rotate()
            //Vec3 axis1 = axis.scale(div * 2.5 + 2).lerp(look, rot);
            var axis1 = VecHelper.rotate(axis.scale(div * 2.5 + 2), rot, look);
            if (axis1.y < 0) {
                axis1 = axis1.multiply(1, -1, 1);
            }

            Vec3 end = pl.add(axis1);

            TrueShadowKatanaProjectile proj = new TrueShadowKatanaProjectile(player.level(), player, damageTime);
            proj.setPos(end.x, end.y, end.z);
            proj.setTargetPos(targetpos);
            proj.faceTargetAccurately(0.75F);
            proj.setDeltaMovement(proj.getDeltaMovement().scale(speedTime));
            player.level().addFreshEntity(proj);
        }
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void attack(LivingEntity player, Entity target) {
        attack(player, target, 3, 0.8D, 1F);
    }

    @Override
    public int getManaPerDamage() {
        return 800;
    }
}
