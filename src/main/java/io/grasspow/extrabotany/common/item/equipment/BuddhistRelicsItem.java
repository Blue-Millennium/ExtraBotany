package io.grasspow.extrabotany.common.item.equipment;

import io.grasspow.extrabotany.common.item.ExtraBotanyItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.common.item.relic.RelicItem;

public class BuddhistRelicsItem extends RelicItem {

    public static final String TAG_MORPHING = "buddhist:morphing";
    public static final String TAG_DATA = "buddhist:data";
    public static final String TAG_HASDATA = "buddhist:hasdata";
    public static final int MANA_PER_DAMAGE = 4;

    public BuddhistRelicsItem(Properties props) {
        super(props);
        MinecraftForge.EVENT_BUS.addListener(this::onItemUpdate);
    }

    @SubscribeEvent
    public void onItemUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player && !event.getEntity().level().isClientSide) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                final ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof Relic) {
                    if (ItemNBTHelper.getBoolean(stack, TAG_MORPHING, false)) {
                        if (ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE, false)) {
                            ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE, true);
                        } else {
                            ItemStack budd = expired(stack);
                            if (!budd.isEmpty()) {
                                player.getInventory().setItem(i, budd);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void relicInit(ItemStack stack) {
        if (!ItemNBTHelper.getBoolean(stack, TAG_HASDATA, false)) {
            ItemStackHandler handler = new ItemStackHandler(5);
            handler.setStackInSlot(0, new ItemStack(ExtraBotanyItems.EXCALIBER.get()));
            handler.setStackInSlot(1, new ItemStack(ExtraBotanyItems.INFINITE_WINE.get()));
            handler.setStackInSlot(2, new ItemStack(ExtraBotanyItems.FAILNAUGHT.get()));
            handler.setStackInSlot(3, new ItemStack(BotaniaItems.infiniteFruit));
            handler.setStackInSlot(4, new ItemStack(BotaniaItems.kingKey));
            CompoundTag data = handler.serializeNBT();
            ItemNBTHelper.setCompound(stack, TAG_DATA, data);
            ItemNBTHelper.setBoolean(stack, TAG_HASDATA, true);
        }
    }

    public static ItemStack expired(ItemStack morphstack) {
        if (ItemNBTHelper.getBoolean(morphstack, TAG_MORPHING, false)) {

            CompoundTag data = ItemNBTHelper.getCompound(morphstack, TAG_DATA, false);
            ItemStackHandler handler = new ItemStackHandler(5);
            handler.deserializeNBT(data);
            int id = 0;
            for (int i = 0; i < 5; i++) {
                ItemStack stack = handler.getStackInSlot(i).copy();
                if (morphstack.getItem() == stack.getItem()) {
                    id = i;
                    break;
                }
            }

            ItemStack budd = new ItemStack(ExtraBotanyItems.BUDDHIST_RELICS.get());
            ItemStack copy = morphstack.copy();
            copy.getOrCreateTag().remove(TAG_DATA);
            handler.setStackInSlot(id, copy);
            ItemNBTHelper.setCompound(budd, TAG_DATA, handler.serializeNBT());
            ItemNBTHelper.setBoolean(budd, TAG_HASDATA, true);
            return budd.copy();
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack relicShift(ItemStack heldstack) {
        if (heldstack.getItem() == ExtraBotanyItems.BUDDHIST_RELICS.get()) {
            relicInit(heldstack);
            CompoundTag data = ItemNBTHelper.getCompound(heldstack, TAG_DATA, false);
            ItemStackHandler handler = new ItemStackHandler(5);
            handler.deserializeNBT(data);
            ItemStack stack = handler.getStackInSlot(0).copy();
            ItemNBTHelper.setBoolean(stack, TAG_MORPHING, true);
            ItemNBTHelper.setCompound(stack, TAG_DATA, data);
            return stack.copy();
        } else if (ItemNBTHelper.getBoolean(heldstack, TAG_MORPHING, false)) {
            CompoundTag data = ItemNBTHelper.getCompound(heldstack, TAG_DATA, false);
            ItemStackHandler handler = new ItemStackHandler(5);
            handler.deserializeNBT(data);
            int id = 0;
            for (int i = 0; i < 5; i++) {
                ItemStack stack = handler.getStackInSlot(i).copy();
                if (heldstack.getItem() == stack.getItem()) {
                    id = i;
                    break;
                }
            }
            if (id == 4) {
                ItemStack budd = new ItemStack(ExtraBotanyItems.BUDDHIST_RELICS.get());
                ItemStack copy = heldstack.copy();
                copy.getOrCreateTag().remove(TAG_DATA);
                handler.setStackInSlot(4, copy);
                ItemNBTHelper.setCompound(budd, TAG_DATA, handler.serializeNBT());
                ItemNBTHelper.setBoolean(budd, TAG_HASDATA, true);
                return budd.copy();
            }
            ItemStack morph = handler.getStackInSlot(id + 1);
            ItemStack copy = heldstack.copy();
            copy.getOrCreateTag().remove(TAG_DATA);
            handler.setStackInSlot(id, copy);
            ItemNBTHelper.setBoolean(morph, TAG_MORPHING, true);
            ItemNBTHelper.setCompound(morph, TAG_DATA, handler.serializeNBT());
            return morph.copy();
        }
        return ItemStack.EMPTY;
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }
}
