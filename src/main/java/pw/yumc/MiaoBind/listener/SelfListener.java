package pw.yumc.MiaoBind.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pw.yumc.MiaoBind.event.BindItemEvent;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;

public class SelfListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemSoulbound(final BindItemEvent event) {
        final Player player = event.getPlayer();
        final Inventory inventory = player.getInventory();
        int maxAmount = Integer.MAX_VALUE;
        if (maxAmount < 0) { return; }
        int count = 0;
        for (final ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && ItemKit.isBind(itemStack)) {
                count++;
            }
        }
        if (count >= maxAmount) {
            Log.sender(player, "§c无法绑定更多的物品, 已到达最大上限! (%s)", maxAmount);
            event.setCancelled(true);
        }
    }
}
