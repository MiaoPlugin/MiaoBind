package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.runnable.CheckArmor;
import pw.yumc.YumCore.bukkit.P;

public class InventoryListener implements Listener {
    private Config config;

    public InventoryListener(Config config) {
        this.config = config;
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        final HumanEntity entity = event.getWhoClicked();
        if (!(entity instanceof Player)) { return; }
        final Player player = (Player) entity;
        final ItemStack itemStack = event.getCurrentItem();
        final InventoryType inventoryType = event.getInventory().getType();
        if (inventoryType == null) { return; }
        if (ItemKit.getItemType(itemStack) != ItemKit.ItemType.MiaoBind) { return; }
        if ((!config.AllowStore && inventoryType != InventoryType.CRAFTING) || (!ItemKit.isBindedPlayer(player, itemStack) && !player.isOp())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveEvent(final InventoryMoveItemEvent event) {
        final ItemStack itemStack = event.getItem();
        final ItemKit.ItemType itemType = ItemKit.getItemType(itemStack);
        if (itemType == ItemKit.ItemType.MiaoBind) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryPickupItemEvent(final InventoryPickupItemEvent event) {
        final ItemStack itemStack = event.getItem().getItemStack();
        final ItemKit.ItemType itemType = ItemKit.getItemType(itemStack);
        if (itemType == ItemKit.ItemType.MiaoBind) {
            event.setCancelled(true);
        }
    }

    /**
     * Check EnchantItemEvent events.
     *
     * @param event
     *            The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnchantItem(final EnchantItemEvent event) {
        final Player player = event.getEnchanter();
        final ItemStack itemStack = event.getItem();
        if (ItemKit.isBindOnUse(itemStack)) {
            ItemKit.bindItem(player, itemStack);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent event) {
        final HumanEntity entity = event.getWhoClicked();
        final ItemStack itemStack = event.getCurrentItem();
        final SlotType slotType = event.getSlotType();
        final InventoryType inventoryType = event.getInventory().getType();
        if (inventoryType == null) { return; }
        if (itemStack == null) { return; }
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            switch (slotType) {
            case ARMOR:
                new CheckArmor(player).runTaskLater(P.instance, 2);
                return;
            case CONTAINER:
                final ItemKit.ItemType itemType = ItemKit.getItemType(itemStack);
                if (itemType == ItemKit.ItemType.BIND_ON_PICKUP) {
                    ItemKit.bindItem(player, itemStack);
                }
            default:
                if (ItemKit.ArmorKit.isEquipable(itemStack) && event.isShiftClick()) {
                    new CheckArmor(player).runTaskLater(P.instance, 2);
                    return;
                }
                break;
            }
        }
    }
}
