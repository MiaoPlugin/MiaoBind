package pw.yumc.MiaoBind.kit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.config.Tag;
import pw.yumc.MiaoBind.event.BindItemEvent;
import pw.yumc.YumCore.bukkit.Log;

public class ItemKit {
    private static String TimeTag = "§卐 ";
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
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            if (isBind(itemStack)) {
                Log.d("判断为绑定物品");
                return ItemType.MiaoBind;
            }
            if (isBindOnTime(itemStack)) {
                Log.d("判断为时间绑定物品");
                return ItemType.MiaoTimeBind;
            }
            if (isBindOnPickup(itemStack)) {
                Log.d("判断为拾取绑定物品");
                return ItemType.BIND_ON_PICKUP;
            }
            if (isBindOnUse(itemStack)) {
                Log.d("判断为使用绑定物品");
                return ItemType.BIND_ON_USE;
            }
            if (isBindOnEquip(itemStack)) {
                Log.d("判断为装备绑定物品");
                return ItemType.BIND_ON_EQUIP;
            }
        }
        Log.d("判断为普通物品");
        return ItemType.NORMAL;
    }

    public static boolean isBoundPlayer(final Player player, final ItemStack itemStack) {
        final List<String> itemLore = Optional.ofNullable(itemStack).map(ItemStack::getItemMeta).map(ItemMeta::getLore).orElse(null);
        if (itemLore == null) {
            return false;
        }
        boolean base = itemLore.contains(player.getName()) || itemLore.contains(addColorChar(player.getName()));
        // 最后一个用于兼容Ess的生成物品
        return base || itemLore.contains(player.getName().replaceAll("_", " "));
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
        return getBindTimeIndex(itemStack) != -1;
    }

    public static int getBindTimeIndex(final ItemStack itemStack) {
        try {
            List<String> lores = itemStack.getItemMeta().getLore();
            for (String tag : Tag.TimeBind) {
                for (String lore : lores) {
                    if (lore.startsWith(tag)) { return lores.indexOf(lore); }
                }
            }
        } catch (NullPointerException ignored) {
        }
        return -1;
    }

    public static boolean isBindOnTag(final ItemStack itemStack, List<String> tags) {
        Log.d("判断物品: %s 判断标签: %s", itemStack, tags);
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

    /**
     * 判断物品是否有效
     *
     * @param itemStack
     *         物品
     * @return 是否有效
     */
    public static boolean isInvalidItem(final ItemStack itemStack) {
        long time = getBindTime(itemStack);
        return time != 0 && time <= System.currentTimeMillis();
    }

    /**
     * 获得物品过期时间戳
     *
     * @param itemStack
     *         物品
     * @return 绑定时间 <br>
     * -1 错误的格式<br>
     * 0 非时间绑定
     */
    public static long getBindTime(final ItemStack itemStack) {
        int index = getBindTimeIndex(itemStack);
        if (index != -1) {
            Log.d("判断为时间绑定物品!");
            String time = itemStack.getItemMeta().getLore().get(index);
            if (time.contains(TimeTag)) {
                String date = time.split(TimeTag, 2)[1];
                try {
                    return Config.DateFormat.parse(date).getTime();
                } catch (ParseException e) {
                    return -1;
                }
            }
        }
        return 0;
    }

    public static ItemStack bindItem(final Player player, ItemStack itemStack) {
        if (itemStack == null) { return null; }
        if (isBind(itemStack)) { return itemStack; }
        Log.d("玩家 %s 绑定物品 %s", player, itemStack);
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

    public static ItemStack bindTimeItem(final Player player, ItemStack itemStack, String time) {
        if (itemStack == null) { return null; }
        if (isBind(itemStack)) { return itemStack; }
        Log.d("玩家 %s 绑定时限物品 %s", player, itemStack);
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
        lores.add(Tag.TimeBind.get(0) + TimeTag + time);
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

    public static void removeTag(List<String> lores) {
        lores.removeAll(Tag.BindOnUse);
        lores.removeAll(Tag.BindOnEquip);
        lores.removeAll(Tag.BindOnPickup);
    }

    public static ItemStack unbindItem(final ItemStack itemStack) {
        if (itemStack == null) { return null; }
        Log.d("解绑物品 %s", itemStack);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore() && isBind(itemStack)) {
            List<String> itemLore = new ArrayList<>(itemMeta.getLore());
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
        /**
         * 普通物品
         */
        NORMAL,
        MiaoBind,
        MiaoTimeBind,
        BIND_ON_USE,
        BIND_ON_EQUIP,
        BIND_ON_PICKUP
    }

    public static class ArmorKit {
        private static List<Material> IronArmor = Arrays.asList(Material.IRON_BOOTS,
                                                                Material.IRON_CHESTPLATE,
                                                                Material.IRON_HELMET,
                                                                Material.IRON_LEGGINGS);
        private static List<Material> GoldArmor = Arrays.asList(Material.GOLD_BOOTS,
                                                                Material.GOLD_CHESTPLATE,
                                                                Material.GOLD_HELMET,
                                                                Material.GOLD_LEGGINGS);
        private static List<Material> DiamondArmor = Arrays.asList(Material.DIAMOND_BOOTS,
                                                                   Material.DIAMOND_CHESTPLATE,
                                                                   Material.DIAMOND_HELMET,
                                                                   Material.DIAMOND_LEGGINGS);
        private static List<Material> LeatherArmor = Arrays.asList(Material.LEATHER_BOOTS,
                                                                   Material.LEATHER_CHESTPLATE,
                                                                   Material.LEATHER_HELMET,
                                                                   Material.LEATHER_LEGGINGS);
        private static List<Material> ChainmailArmor = Arrays.asList(Material.CHAINMAIL_BOOTS,
                                                                     Material.CHAINMAIL_CHESTPLATE,
                                                                     Material.CHAINMAIL_HELMET,
                                                                     Material.CHAINMAIL_LEGGINGS);

        /**
         * Checks to see if an item is a chainmail armor piece.
         *
         * @param is
         *         Item to check
         * @return true if the item is chainmail armor, false otherwise
         */
        public static boolean isChainmailArmor(final ItemStack is) {
            return ChainmailArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a diamond armor piece.
         *
         * @param is
         *         Item to check
         * @return true if the item is diamond armor, false otherwise
         */
        public static boolean isDiamondArmor(final ItemStack is) {
            return DiamondArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a gold armor piece.
         *
         * @param is
         *         Item to check
         * @return true if the item is gold armor, false otherwise
         */
        public static boolean isGoldArmor(final ItemStack is) {
            return GoldArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is an iron armor piece.
         *
         * @param is
         *         Item to check
         * @return true if the item is iron armor, false otherwise
         */
        public static boolean isIronArmor(final ItemStack is) {
            return IronArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a leather armor piece.
         *
         * @param is
         *         Item to check
         * @return true if the item is leather armor, false otherwise
         */
        public static boolean isLeatherArmor(final ItemStack is) {
            return LeatherArmor.contains(is.getType());
        }

        /**
         * Checks to see if an item is a wearable armor piece.
         *
         * @param is
         *         Item to check
         * @return true if the item is armor, false otherwise
         */
        public static boolean isMinecraftArmor(final ItemStack is) {
            return isLeatherArmor(is) || isGoldArmor(is) || isIronArmor(is) || isDiamondArmor(is) || isChainmailArmor(is);
        }

        /**
         * Checks to see if an item is an equipable item.
         *
         * @param is
         *         Item to check
         * @return true if the item is equipable, false otherwise
         */
        public static boolean isEquipable(final ItemStack is) {
            return !(is == null || is.getType() == Material.AIR) &&
                   (isMinecraftArmor(is) || is.getType() == Material.SKULL_ITEM || is.getType() == Material.JACK_O_LANTERN);
        }
    }
}
