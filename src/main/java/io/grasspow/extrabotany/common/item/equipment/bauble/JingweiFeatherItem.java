package io.grasspow.extrabotany.common.item.equipment.bauble;

import io.grasspow.extrabotany.api.item.IItemWithLeftClick;
import io.grasspow.extrabotany.common.entity.projectile.AuraFireProjectile;
import io.grasspow.extrabotany.common.network.LeftClickPack;
import io.grasspow.extrabotany.xplat.client.ClientModXplatAbstractions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.handler.EquipmentHandler;

public class JingweiFeatherItem extends BaubleItem implements IItemWithLeftClick {

    public static final int MANA_PER_DAMAGE = 300;

    public JingweiFeatherItem(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.addListener(this::leftClick);
        MinecraftForge.EVENT_BUS.addListener(this::leftClickBlock);
        MinecraftForge.EVENT_BUS.addListener(this::attackEntity);
    }

    public void attackEntity(AttackEntityEvent evt) {
        if (!evt.getEntity().level().isClientSide()) {
            if (!EquipmentHandler.findOrEmpty(this, evt.getEntity()).isEmpty())
                onLeftClick(evt.getEntity(), evt.getTarget());
        }
    }

    public void leftClick(PlayerInteractEvent.LeftClickEmpty evt) {
        if (!EquipmentHandler.findOrEmpty(this, evt.getEntity()).isEmpty())
            ClientModXplatAbstractions.INSTANCE.sendToServer(LeftClickPack.INSTANCE);

    }

    public void leftClickBlock(PlayerInteractEvent.LeftClickBlock evt) {
        if (evt.getEntity().level().isClientSide() && !EquipmentHandler.findOrEmpty(this, evt.getEntity()).isEmpty())
            ClientModXplatAbstractions.INSTANCE.sendToServer(LeftClickPack.INSTANCE);
    }

    @Override
    public void onLeftClick(Player player, Entity target) {
        if (player.getMainHandItem().isEmpty() && player.getAttackStrengthScale(0) == 1)
            if (ManaItemHandler.instance().requestManaExactForTool(new ItemStack(this), player, MANA_PER_DAMAGE, true)) {
                AuraFireProjectile proj = new AuraFireProjectile(player.level(), player);
                proj.setPos(player.getX(), player.getY() + 1.1D, player.getZ());
                proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.8F, 0.9F);
                if (!player.level().isClientSide())
                    player.level().addFreshEntity(proj);
            }
    }

}
