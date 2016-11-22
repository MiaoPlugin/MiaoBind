package pw.yumc.MiaoBind.runnable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import pw.yumc.MiaoBind.kit.ItemKit;

/**
 * @author 喵♂呜
 * @since 2016/11/23 0023
 */
public class CheckTimeBind extends BukkitRunnable {
    private Player player;

    public CheckTimeBind(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player != null && player.isValid()) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (!ItemKit.isValidItem(item)) {
                    item.setType(Material.AIR);
                }
            }
            player.getInventory().setContents(player.getInventory().getContents());
        }
    }
}
