package pw.yumc.MiaoBind.runnable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;

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
        Log.d("绑定装备检查!");
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null) {
                ItemKit.ItemType type = ItemKit.getItemType(armor);
                if (type == ItemKit.ItemType.BIND_ON_EQUIP) {
                    ItemKit.bindItem(player, armor);
                } else if (type == ItemKit.ItemType.MiaoTimeBind && !ItemKit.isValidItem(armor)) {
                    armor.setType(Material.AIR);
                }
            }
        }
        player.getInventory().setArmorContents(player.getInventory().getArmorContents());
    }
}
