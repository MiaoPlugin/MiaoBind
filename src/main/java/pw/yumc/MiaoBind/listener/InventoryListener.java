package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.runnable.CheckArmor;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.bukkit.P;

import java.util.Optional;

/**
 * @author MiaoWoo
 */
public class InventoryListener implements Listener {
    private Config config = P.getInjectConfig();

    public InventoryListener() {
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        final HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) {return;}
        final Player player = (Player) entity;
        ItemStack itemStack = event.getCurrentItem();
        ItemKit.ItemType itemType = ItemKit.getItemType(itemStack);
        if (itemType != ItemKit.ItemType.MiaoBind && itemType != ItemKit.ItemType.MiaoTimeBind) {
            if (!event.getAction().name().startsWith("HOTBAR")) {return;}
            itemStack = player.getInventory().getItem(event.getHotbarButton());
            Log.d("Action: %s Item: %s", event.getAction().name(), itemStack);
            itemType = ItemKit.getItemType(itemStack);
            if (itemType != ItemKit.ItemType.MiaoBind && itemType != ItemKit.ItemType.MiaoTimeBind) {return;}
        }
        if (player.getInventory().firstEmpty() == -1) {
            Log.d("[InventoryListener onInventoryClickEvent] notFirstEmpty");
            Log.sender(player, "§c背包已满的情况下禁止点击绑定物品 防止物品意外丢失!");
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            return;
        }
        if (player.isOp()) {
            if (!ItemKit.isBoundPlayer(player, itemStack)) {
                Log.sender(player, "§c当前物品已被绑定 由于您是OP 已忽略限制!");
            }
            return;
        }
        if (!ItemKit.isBoundPlayer(player, itemStack)) {
            Log.sender(player, "§c当前物品已被绑定 非持有者无法获取!");
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
        final InventoryType inventoryType = event.getInventory().getType();
        boolean isCraftInv = inventoryType == InventoryType.CRAFTING || inventoryType == InventoryType.ANVIL;
        if (!config.AllowStore && !isCraftInv) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    /**
     * Check EnchantItemEvent events.
     *
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchantItem(final EnchantItemEvent event) {
        final Player player = event.getEnchanter();
        final ItemStack itemStack = event.getItem();
        if (ItemKit.isBindOnUse(itemStack)) {
            ItemKit.bindItem(player, itemStack);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryDrop(final InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        InventoryAction action = event.getAction();
        ItemStack itemStack = Optional.ofNullable(event.getCurrentItem()).orElse(event.getCursor());
        if (itemStack != null && entity instanceof Player) {
            final Player player = (Player) entity;
            if (action.name().startsWith("DROP") && ItemKit.isBoundPlayer(player, itemStack)) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        ItemStack itemStack = event.getCursor();
        SlotType slotType = event.getSlotType();
        InventoryAction action = event.getAction();
        Log.d("InventoryClickEvent Action: %s Click: %s SlotType: %s Slot: %s RawSlot: %s HotbarButton: %s",
                action.name(), event.getClick().name(), slotType.name(),
                event.getSlot(), event.getRawSlot(), event.getHotbarButton());
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            itemStack = event.getCurrentItem();
        }
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            switch (slotType) {
                case ARMOR:
                    new CheckArmor(player).runTaskLater(P.instance, 2);
                    return;
                case CONTAINER:
                    if (ItemKit.isBindOnUse(itemStack)) {
                        ItemKit.bindItem(player, itemStack);
                    }
                default:
            }
            boolean isArmor = ItemKit.ArmorKit.isEquipable(itemStack) && event.isShiftClick();
            boolean isExtra = event.getClickedInventory() instanceof PlayerInventory && event.getRawSlot() > 44;
            if (isArmor || isExtra) {
                new CheckArmor(player).runTaskLater(P.instance, 2);
            }
        }
    }
}
