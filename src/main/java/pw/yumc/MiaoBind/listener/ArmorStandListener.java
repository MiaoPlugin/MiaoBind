package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.bukkit.P;

public class ArmorStandListener implements Listener {
    public ArmorStandListener() {
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getPlayerItem();
        if (itemStack != null && ItemKit.isBind(itemStack)) {
            Log.sender(player, "§c绑定物品不允许被放置在盔甲架上.");
            event.setCancelled(true);
        }
    }
}
