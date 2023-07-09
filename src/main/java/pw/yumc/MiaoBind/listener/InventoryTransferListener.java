package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.P;

public class InventoryTransferListener implements Listener {
    public InventoryTransferListener() {
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveEvent(final InventoryMoveItemEvent event) {
        final ItemStack itemStack = event.getItem();
        if (ItemKit.isBind(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryPickupItemEvent(final InventoryPickupItemEvent event) {
        final ItemStack itemStack = event.getItem().getItemStack();
        if (ItemKit.isBind(itemStack)) {
            event.setCancelled(true);
        }
    }
}
