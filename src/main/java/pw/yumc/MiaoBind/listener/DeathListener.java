package pw.yumc.MiaoBind.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import pw.yumc.MiaoBind.config.Data;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.runnable.UpdateInventory;
import pw.yumc.YumCore.bukkit.P;

public class DeathListener implements Listener {
    private Data config;

    public DeathListener(Data config) {
        this.config = config;
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> bis = new ArrayList<>();
        List<ItemStack> items = e.getDrops();
        for (ItemStack item : items) {
            if (ItemKit.isBind(item)) {
                bis.add(item);
            }
        }
        if (!bis.isEmpty()) {
            config.bindItems.put(e.getEntity().getName(), bis);
            config.save();
            items.removeAll(bis);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerRespawnHighest(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission("MiaoBind.keepondeath")) { return; }
        List<ItemStack> items = config.bindItems.remove(player.getName());
        config.save();
        if (items != null && !items.isEmpty()) {
            player.getInventory().addItem(items.toArray(new ItemStack[] {}));
        }
        new UpdateInventory(player).runTask(P.instance);
    }
}
