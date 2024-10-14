package io.grasspow.extrabotany.client.model.armor;

import io.grasspow.extrabotany.client.model.ExtraBotanyModelLayers;
import io.grasspow.extrabotany.common.item.equipment.armor.GoblinsLayerArmorItem;
import io.grasspow.extrabotany.common.item.equipment.armor.MaidArmorItem;
import io.grasspow.extrabotany.common.item.equipment.armor.MikuArmorItem;
import io.grasspow.extrabotany.common.item.equipment.armor.ShadowWarriorArmorItem;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.client.model.armor.ArmorModel;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ArmorModels {
    private static Map<EquipmentSlot, ArmorModel> miku = Collections.emptyMap();
    private static Map<EquipmentSlot, ArmorModel> maid = Collections.emptyMap();
    private static Map<EquipmentSlot, ArmorModel> goblins_layer = Collections.emptyMap();
    private static Map<EquipmentSlot, ArmorModel> shadow_warrior = Collections.emptyMap();
//	private static Map<EquipmentSlot, ArmorModel> shooting_guardian = Collections.emptyMap();
//	private static Map<EquipmentSlot, ArmorModel> silent_sages = Collections.emptyMap();

    private static Map<EquipmentSlot, ArmorModel> make(EntityRendererProvider.Context ctx, ModelLayerLocation inner, ModelLayerLocation outer) {
        Map<EquipmentSlot, ArmorModel> ret = new EnumMap<>(EquipmentSlot.class);
        for (var slot : EquipmentSlot.values()) {
            var mesh = ctx.bakeLayer(slot == EquipmentSlot.LEGS ? inner : outer);
            ret.put(slot, new ArmorModel(mesh, slot));
        }
        return ret;
    }

    public static void init(EntityRendererProvider.Context ctx) {
        miku = make(ctx, ExtraBotanyModelLayers.MIKU_INNER, ExtraBotanyModelLayers.MIKU_OUTER);
        maid = make(ctx, ExtraBotanyModelLayers.MAID_INNER, ExtraBotanyModelLayers.MAID_OUTER);
        goblins_layer = make(ctx, ExtraBotanyModelLayers.GOBLIN_SLAYER_INNER, ExtraBotanyModelLayers.GOBLIN_SLAYER_OUTER);
        shadow_warrior = make(ctx, ExtraBotanyModelLayers.SHADOW_INNER_ARMOR, ExtraBotanyModelLayers.SHADOW_OUTER_ARMOR);
//		shooting_guardian = make(ctx, BotaniaModelLayers.TERRASTEEL_INNER_ARMOR, BotaniaModelLayers.TERRASTEEL_OUTER_ARMOR);
//		silent_sages = make(ctx, BotaniaModelLayers.TERRASTEEL_INNER_ARMOR, BotaniaModelLayers.TERRASTEEL_OUTER_ARMOR);
    }

    @Nullable
    public static ArmorModel get(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ShadowWarriorArmorItem armor) {
            return shadow_warrior.get(armor.getEquipmentSlot());
        } else if (item instanceof GoblinsLayerArmorItem armor) {
            return goblins_layer.get(armor.getEquipmentSlot());
        } else if (item instanceof MaidArmorItem armor) {
            return maid.get(armor.getEquipmentSlot());
        } else if (item instanceof MikuArmorItem armor) {
            return miku.get(armor.getEquipmentSlot());
        }

        return null;
    }
}
