package pw.yumc.MiaoBind.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.config.Tag;
import pw.yumc.MiaoBind.event.BindItemEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemKit {
    private static Config Config;
    private static Tag Tag;

    public static void init(Config config) {
        ItemKit.Config = config;
        ItemKit.Tag = config.Tag;
    }

    public static ItemStack boeItem(final ItemStack itemStack) {
        if (itemStack == null) { return null; }
        if (isBindOnEquip(itemStack)) { return itemStack; }
        return addTag(itemStack, Tag.BindOnEquip.get(0));
    }

    public static ItemStack bopItem(final ItemStack itemStack) {
        if (itemStack == null) { return null; }
        if (isBindOnPickup(itemStack)) { return itemStack; }
        return addTag(itemStack, Tag.BindOnPickup.get(0));
    }

    public static ItemStack bouItem(final ItemStack itemStack) {
        if (itemStack == null) { return null; }
        if (isBindOnUse(itemStack)) { return itemStack; }
        return addTag(itemStack, Tag.BindOnUse.get(0));
    }

    public static ItemType getItemType(final ItemStack itemStack) {
        if (itemStack != null) {
            if (isBind(itemStack)) { return ItemType.MiaoBind; }
            if (isBindOnPickup(itemStack)) { return ItemType.BIND_ON_PICKUP; }
            if (isBindOnUse(itemStack)) { return ItemType.BIND_ON_USE; }
            if (isBindOnEquip(itemStack)) { return ItemType.BIND_ON_EQUIP; }
        }
        return ItemType.NORMAL;
    }

    public static boolean isBindedPlayer(final Player player, final ItemStack itemStack) {
        final List<String> itemLore = itemStack.getItemMeta().getLore();
        return itemLore.contains(player.getName()) || itemLore.contains(player.getName().replaceAll("_", " "));
    }

    public static boolean isBindOnEquip(final ItemStack itemStack) {
        return isBindOnTag(itemStack, Tag.BindOnEquip);
    }

    public static boolean isBindOnPickup(final ItemStack itemStack) {
        return isBindOnTag(itemStack, Tag.BindOnPickup);
    }

    public static boolean isBindOnUse(final ItemStack itemStack) {
        return isBindOnTag(itemStack, Tag.BindOnUse);
    }

    public static boolean isBindOnTime(final ItemStack itemStack) {
        try {
            List<String> lores = itemStack.getItemMeta().getLore();
            for (String tag : Tag.TimeBind) {
                for (String lore : lores) {
                    if (lore.startsWith(tag)) { return true; }
                }
            }
        } catch (NullPointerException ignored) {
        }
        return false;
    }

    public static boolean isBindOnTag(final ItemStack itemStack, List<String> tags) {
        try {
            List<String> lores = itemStack.getItemMeta().getLore();
            for (String tag : tags) {
                if (lores.contains(tag)) { return true; }
            }
        } catch (NullPointerException ignored) {
        }
        return false;
    }

    public static boolean isNormalItem(final ItemStack itemStack) {
        return !itemStack.hasItemMeta() && !itemStack.getItemMeta().hasLore() || ItemKit.getItemType(itemStack) == ItemType.NORMAL;
    }

    public static boolean isBind(final ItemStack itemStack) {
        return isBindOnTag(itemStack, Tag.Bind) || isBindOnTime(itemStack);
    }

    public static ItemStack bindItem(final Player player, ItemStack itemStack) {
        if (itemStack == null) { return null; }
        if (isBind(itemStack)) { return itemStack; }
        final BindItemEvent bindItemEvent = new BindItemEvent(player, itemStack);
        Bukkit.getPluginManager().callEvent(bindItemEvent);
        itemStack = bindItemEvent.getItemStack();
        if (bindItemEvent.isCancelled()) { return itemStack; }
        List<String> lores = new ArrayList<>();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }
        if (itemMeta.hasLore()) {
            lores.addAll(itemMeta.getLore());
        }
        if (!lores.isEmpty()) {
            removeTag(lores);
        }
        lores.add(Tag.Bind.get(0));
        lores.add(Config.HideName ? addColorChar(player.getName()) : player.getName());
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String addColorChar(String str) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            s.append("§").append(str.charAt(i));
        }
        return s.toString();
    }

    public static List<String> removeTag(List<String> lores) {
        lores.removeAll(Tag.BindOnUse);
        lores.removeAll(Tag.BindOnEquip);
        lores.removeAll(Tag.BindOnPickup);
        return lores;
    }

    public static ItemStack unbindItem(final ItemStack itemStack) {
        if (itemStack == null) { return null; }
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore() && isBind(itemStack)) {
            List<String> itemLore = new ArrayList<>();
            itemLore.addAll(itemMeta.getLore());
            final int index = getIndexOfBindTag(itemLore);
            if (index == -1) { return itemStack; }
            itemLore.remove(index);
            if (index < itemLore.size()) {
                itemLore.remove(index);
            }
            itemMeta.setLore(itemLore.isEmpty() ? null : itemLore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static int getIndexOfBindTag(final List<String> itemLore) {
        final int index = -1;
        for (String tag : Tag.Bind) {
            if (itemLore.contains(tag)) { return itemLore.indexOf(tag); }
        }
        for (String tag : Tag.TimeBind) {
            for (String lore : itemLore) {
                if (lore.startsWith(tag)) { return itemLore.indexOf(lore); }
            }
        }
        return index;
    }

    public static ItemStack addTag(ItemStack itemStack, String tag) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final List<String> itemLore = new ArrayList<>();
        if (itemMeta.hasLore()) {
            itemLore.addAll(itemMeta.getLore());
        }
        itemLore.add(tag);
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public enum ItemType {
        BIND_ON_EQUIP,
        BIND_ON_PICKUP,
        BIND_ON_USE,
        NORMAL,
        MiaoBind
    }

    public static class ArmorKit {
        private static List<Material> IronArmor = Arrays.asList(Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS);
        private static List<Material> GoldArmor = Arrays.asList(Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET, Material.GOLD_LEGGINGS);
        private static List<Material> DiamondArmor = Arrays.asList(Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS);
        private static List<Material> LeatherArmor = Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS);
        private static List<Material> ChainmailArmor = Arrays.asList(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS);

        /**
         * Checks to see if an item is a chainmail armor piece.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is chainmail armor, false otherwise
         */
        public static boolean isChainmailArmor(final ItemStack is) {
            return ChainmailArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a diamond armor piece.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is diamond armor, false otherwise
         */
        public static boolean isDiamondArmor(final ItemStack is) {
            return DiamondArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a gold armor piece.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is gold armor, false otherwise
         */
        public static boolean isGoldArmor(final ItemStack is) {
            return GoldArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is an iron armor piece.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is iron armor, false otherwise
         */
        public static boolean isIronArmor(final ItemStack is) {
            return IronArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a leather armor piece.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is leather armor, false otherwise
         */
        public static boolean isLeatherArmor(final ItemStack is) {
            return LeatherArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a wearable armor piece.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is armor, false otherwise
         */
        public static boolean isMinecraftArmor(final ItemStack is) {
            return isLeatherArmor(is) || isGoldArmor(is) || isIronArmor(is) || isDiamondArmor(is) || isChainmailArmor(is);
        }

        /**
         * Checks to see if an item is an equipable item.
         *
         * @param is
         *            Item to check
         *
         * @return true if the item is equipable, false otherwise
         */
        public static boolean isEquipable(final ItemStack is) {
            return isMinecraftArmor(is) || is.getType() == Material.SKULL_ITEM || is.getType() == Material.JACK_O_LANTERN;
        }
    }
}
