package pw.yumc.MiaoBind.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.runnable.UpdateInventory;
import pw.yumc.YumCore.bukkit.P;

public class PlayerListener implements Listener {
    private Config config;
    private Sound sound;

    public PlayerListener(Config config) {
        this.config = config;
        try {
            sound = Sound.valueOf("ITEM_BREAK");
        } catch (IllegalArgumentException e) {
            sound = Sound.ITEM_SHIELD_BREAK;
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
        if (ItemKit.isBind(itemStack) && !ItemKit.isBindedPlayer(player, itemStack)) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
            return;
        }
        if (ItemKit.isBindOnPickup(itemStack)) {
            ItemKit.bindItem(player, itemStack);
        }
        if (player.getItemOnCursor() != null && ItemKit.isBind(player.getItemOnCursor())) {
            item.setPickupDelay(40);
            event.setCancelled(true);
        }
    }
}
