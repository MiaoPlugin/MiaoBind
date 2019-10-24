package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import pw.yumc.MiaoBind.runnable.CheckArmor;
import pw.yumc.YumCore.bukkit.P;

/**
 * @author MiaoWoo
 */
public class SwapHandItemListener implements Listener {
    public SwapHandItemListener() {
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwitchHand(PlayerSwapHandItemsEvent event) {
        new CheckArmor(event.getPlayer()).runTaskAsynchronously(P.instance);
    }
}
