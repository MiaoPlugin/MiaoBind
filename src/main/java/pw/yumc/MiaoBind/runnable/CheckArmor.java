package pw.yumc.MiaoBind.runnable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;

/**
 * @author 喵♂呜
 * @since 2016/11/21 0021
 */
public class CheckArmor extends BukkitRunnable {
    private Player player;
    private static boolean checkExtra = true;

    static {
        try {
            PlayerInventory.class.getMethod("getExtraContents");
        } catch (Exception ex) {
            Log.d("忽略扩展装备栏...", ex.getMessage());
            checkExtra = false;
        }
    }

    public CheckArmor(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        Log.d("绑定装备检查!");
        player.getInventory().setArmorContents(checkContents(player.getInventory().getArmorContents()));
        if (checkExtra) {
            player.getInventory().setExtraContents(checkContents(player.getInventory().getExtraContents()));
        }
    }

    private ItemStack[] checkContents(ItemStack[] items) {
        for (final ItemStack armor : items) {
            if (armor != null) {
                ItemKit.ItemType type = ItemKit.getItemType(armor);
                if (type == ItemKit.ItemType.BIND_ON_EQUIP) {
                    ItemKit.bindItem(player, armor);
                } else if (type == ItemKit.ItemType.MiaoTimeBind && ItemKit.isInvalidItem(armor)) {
                    armor.setType(Material.AIR);
                }
            }
        }
        return items;
    }
}
