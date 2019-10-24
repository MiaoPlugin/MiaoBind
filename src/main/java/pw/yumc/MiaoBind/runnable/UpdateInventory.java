package pw.yumc.MiaoBind.runnable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author MiaoWoo
 */
public class UpdateInventory extends BukkitRunnable {
    private final Player player;

    public UpdateInventory(final Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isValid()) {
            player.updateInventory();
        }
    }
}
