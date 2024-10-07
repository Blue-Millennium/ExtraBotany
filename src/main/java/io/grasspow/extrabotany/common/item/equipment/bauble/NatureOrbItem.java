package io.grasspow.extrabotany.common.item.equipment.bauble;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.CustomCreativeTabContents;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class NatureOrbItem extends BaubleItem implements CustomCreativeTabContents {

    public static final String TAG_XP = "xp";
    public static final int MAX_XP = 500000;

    public NatureOrbItem(Properties props) {
        super(props);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, world, tooltip, flags);
        tooltip.add(Component.translatable("extrabotany.nature_orb", getXP(stack), getMaxXP(stack)).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("extrabotany.nature_orb_effect1").withStyle(getXP(stack) >= 100000 ? ChatFormatting.AQUA : ChatFormatting.GRAY));
        tooltip.add(Component.translatable("extrabotany.nature_orb_effect2").withStyle(getXP(stack) >= 300000 ? ChatFormatting.DARK_RED : ChatFormatting.GRAY));
        tooltip.add(Component.translatable("extrabotany.nature_orb_effect3").withStyle(getXP(stack) >= 400000 ? ChatFormatting.DARK_GREEN : ChatFormatting.GRAY));
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        Level level = ctx.getLevel();
        BlockPos clickedPos = ctx.getClickedPos();
//        return EntityEGO.spawn(ctx.getPlayer(), stack, level, clickedPos) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        return InteractionResult.PASS;
    }

    @Override
    public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
        output.accept(new ItemStack(me));
        ItemStack full = new ItemStack(this);
        NatureOrbItem.setXP(full, getMaxXP(full));
        output.accept(full);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getXP(stack) * 13.0F / (float) getMaxXP(stack));
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        super.onWornTick(stack, entity);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!player.level().isClientSide()) {
                if (getXP(stack) > 100000 && player.tickCount % 5 == 0)
                    ManaItemHandler.instance().dispatchManaExact(stack, player, 5, true);
                if (getXP(stack) > 200000 && player.tickCount % 5 == 0)
                    ManaItemHandler.instance().dispatchManaExact(stack, player, 5, true);
                if (getXP(stack) > 300000 && player.tickCount % 5 == 0) {
                    ManaItemHandler.instance().dispatchManaExact(stack, player, 5, true);
                    if (player.tickCount % 60 == 0)
                        player.heal(1F);
                }
                if (getXP(stack) > 400000) {
                    if (player.tickCount % 40 == 0) {
                        clearPotions(stack, player);
                    }
                }
            }
        }
    }

    public static void addXP(ItemStack stack, int xp) {
        if (NatureOrbItem.getXP(stack) > NatureOrbItem.getMaxXP(stack))
            return;
        NatureOrbItem.setXP(stack, Math.min(Math.max(getXP(stack) + xp, 0), getMaxXP(stack)));
    }

    public static void setXP(ItemStack stack, int xp) {
        ItemNBTHelper.setInt(stack, TAG_XP, xp);
    }

    public static int getXP(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_XP, 0);
    }

    public static int getMaxXP(ItemStack stack) {
        return MAX_XP;
    }

    public void clearPotions(ItemStack stack, Player player) {
        List<MobEffect> potionsToRemove = player.getActiveEffectsMap().entrySet().stream()
                .filter(effect -> effect.getKey().getCategory() == MobEffectCategory.HARMFUL
                        && effect.getValue().isCurativeItem(new ItemStack(Items.MILK_BUCKET)))
                .map(Map.Entry::getKey)
                .distinct()
                .toList();

        potionsToRemove.forEach(potion -> {
            player.removeEffect(potion);
            NatureOrbItem.addXP(stack, -50);
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        });
    }
}
