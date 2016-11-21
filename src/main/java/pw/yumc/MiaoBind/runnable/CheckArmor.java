package pw.yumc.MiaoBind.runnable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pw.yumc.MiaoBind.kit.ItemKit;

/**
 * @author 喵♂呜
 * @since 2016/11/21 0021
 */
public class CheckArmor extends BukkitRunnable {
    private Player player;

    public CheckArmor(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        ItemKit.bindArmor(player);
    }
}
