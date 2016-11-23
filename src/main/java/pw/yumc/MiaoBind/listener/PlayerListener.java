package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.runnable.CheckArmor;
import pw.yumc.MiaoBind.runnable.UpdateInventory;
import pw.yumc.YumCore.bukkit.P;

public class PlayerListener implements Listener {
    private Config config;
    private Sound sound;

    public PlayerListener(Config config) {
        this.config = config;
        try {
            sound = Sound.valueOf("ITEM_SHIELD_BREAK");
        } catch (IllegalArgumentException e) {
            sound = Sound.valueOf("ITEM_BREAK");
        }
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler
    public void onPlayerHeldItem(PlayerItemHeldEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();
        ItemStack pre = inv.getItem(event.getPreviousSlot());
        if (!ItemKit.isValidItem(pre)) {
            inv.setItem(event.getPreviousSlot(), null);
        }
        ItemStack newi = inv.getItem(event.getNewSlot());
        if (!ItemKit.isValidItem(newi)) {
            inv.setItem(event.getNewSlot(), null);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        PlayerInventory inv = event.getPlayer().getInventory();
        ItemStack item = event.getItem();
        switch (ItemKit.getItemType(item)) {
        case MiaoTimeBind:
            if (!ItemKit.isValidItem(item)) {
                item.setType(Material.AIR);
            }
            return;
        case BIND_ON_USE:
            ItemKit.bindItem(event.getPlayer(), item);
            return;
        }
        if (ItemKit.ArmorKit.isEquipable(item)) {
            new CheckArmor(event.getPlayer()).runTaskAsynchronously(P.instance);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (!player.isValid()) { return; }
        final Item item = event.getItemDrop();
        final ItemStack itemStack = item.getItemStack();
        if (config.PreventDrop) {
            if (ItemKit.isBind(itemStack) && ItemKit.isBindedPlayer(player, itemStack)) {
                item.setPickupDelay(2 * 20);
                event.setCancelled(true);
                new UpdateInventory(player).runTask(P.instance);
            }
        } else if (config.DeleteOnDrop) {
            player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
            event.getItemDrop().remove();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final Item item = event.getItem();
        final ItemStack itemStack = item.getItemStack();
        switch (ItemKit.getItemType(itemStack)) {
        case MiaoBind:
            if (!ItemKit.isBindedPlayer(player, itemStack) && !player.isOp()) {
                event.setCancelled(true);
                return;
            }
            break;
        case MiaoTimeBind:
            if (!ItemKit.isValidItem(itemStack)) {
                itemStack.setType(Material.AIR);
            }
            break;
        case BIND_ON_PICKUP:
            ItemKit.bindItem(player, itemStack);
            break;
        }
        if (player.getItemOnCursor() != null && ItemKit.isBind(player.getItemOnCursor())) {
            item.setPickupDelay(40);
            event.setCancelled(true);
        }
    }
}
