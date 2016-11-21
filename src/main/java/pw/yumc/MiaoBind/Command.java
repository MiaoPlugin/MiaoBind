package pw.yumc.MiaoBind;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.interfaces.Executor;

/**
 * 命令类
 * 
 * @since 2016/11/18 0018
 * @author 喵♂呜
 */
public class Command implements Executor {
    boolean newVersion = true;
    {
        try {
            PlayerInventory.class.getMethod("getItemInMainHand");
        } catch (NoSuchMethodException e) {
            newVersion = false;
        }
    }

    public ItemStack getItemInHand(Player player) {
        if (newVersion) {
            return player.getInventory().getItemInMainHand();
        } else {
            return player.getItemInHand();
        }
    }

    @Cmd(executor = Cmd.Executor.PLAYER)
    public void bind(Player player, Player target) {
        PlayerInventory a = player.getInventory();
        ItemStack is = getItemInHand(player);
        if (ItemKit.isBind(is)) {
            Log.sender(player, "§c物品已绑定 请勿重复操作!");
            return;
        }
        if (target == null) {
            Log.sender(player, "§c玩家 §b%s §c不在线或不存在!");
            return;
        }
    }

    @Cmd(executor = Cmd.Executor.PLAYER)
    public void bindtime(Player player, String name, String time) {
        Log.sender(player, "bindtime");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "ub")
    public void unbind(Player player) {
        Log.sender(player, "unbind");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "bop")
    public void bindonpickup(Player player) {
        Log.sender(player, "bindonpickup");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "boe")
    public void bindonequip(Player player) {
        Log.sender(player, "bindonequip");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "bou")
    public void bindonuse(Player player) {
        Log.sender(player, "bindonuse");
    }
}
