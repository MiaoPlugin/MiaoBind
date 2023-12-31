package pw.yumc.MiaoBind;

import java.text.ParseException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.annotation.Option;
import pw.yumc.YumCore.commands.annotation.Sort;
import pw.yumc.YumCore.commands.exception.CommandException;
import pw.yumc.YumCore.commands.interfaces.Executor;

/**
 * 命令类
 *
 * @author 喵♂呜
 * @since 2016/11/18 0018
 */
public class Command implements Executor {
    private Config Config;
    private boolean newVersion = true;

    {
        try {
            PlayerInventory.class.getMethod("getItemInMainHand");
        } catch (NoSuchMethodException e) {
            newVersion = false;
        }
    }

    public Command(Config config) {
        Config = config;
    }

    private ItemStack getItemInHand(Player player) {
        if (newVersion) {
            return player.getInventory().getItemInMainHand();
        } else {
            return player.getItemInHand();
        }
    }

    @Sort(1)
    @Cmd(executor = Cmd.Executor.PLAYER, permission = "MiaoBind.bind")
    @Help("绑定物品")
    public void bind(Player player, @Option("check sender") Player target) {
        ItemStack is = check(player);
        ItemKit.bindItem(target == null ? player : target, is);
    }

    @Sort(2)
    @Cmd(minimumArguments = 2, executor = Cmd.Executor.PLAYER, permission = "MiaoBind.bindtime")
    @Help(value = "绑定时限物品", possibleArguments = "<玩家名称> 到期时间[xxxx-xx-xx xx:xx:xx]")
    public void bindtime(Player player, String name, String time) {
        Player target = player;
        ItemStack is = check(player);
        if (time == null) {
            time = name;
        } else {
            target = Bukkit.getPlayerExact(name);
        }
        if (target == null) {
            Log.sender(player, "§c玩家 §b%s §c不存在或不在线!", name);
            return;
        }
        try {
            Config.DateFormat.parse(time);
        } catch (ParseException e) {
            Log.sender(player, "§c时间 §e%s §c格式不正确 应为 §e%s!", time, Config.DateFormat.format(new Date()));
            return;
        }
        ItemKit.bindTimeItem(player, is, time);
        Log.sender(player, "§a物品已设定为 §c时间绑定 将于 §e%s §a过期!", time);
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "ub", permission = "MiaoBind.unbind")
    @Help("解绑物品")
    public void unbind(Player player) {
        ItemStack is = getItemInHand(player);
        if (!ItemKit.isBind(is)) {
            Log.sender(player, "§c物品未绑定 操作失败!");
            return;
        }
        ItemKit.unbindItem(is);
        Log.sender(player, "§a物品解绑成功!");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "bop", permission = "MiaoBind.bindonpickup")
    @Help("拾取时绑定物品")
    public void bindonpickup(Player player) {
        ItemStack is = check(player);
        ItemKit.bopItem(is);
        Log.sender(player, "§a物品已设定为 §c拾取时绑定物品!");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "boe", permission = "MiaoBind.bindonequip")
    @Help("装备时绑定物品")
    public void bindonequip(Player player) {
        ItemStack is = check(player);
        ItemKit.ArmorKit.isEquipable(is);
        ItemKit.boeItem(is);
        Log.sender(player, "§a物品已设定为 §c装备时绑定物品!");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "bou", permission = "MiaoBind.bindonuse")
    @Help("使用时绑定物品")
    public void bindonuse(Player player) {
        ItemStack is = check(player);
        ItemKit.bouItem(is);
        Log.sender(player, "§a物品已设定为 §c使用时绑定物品!");
    }

    private ItemStack check(Player player) {
        ItemStack is = getItemInHand(player);
        if (is == null || is.getType() == Material.AIR) {
            throw new CommandException("§c空手无法进行操作!");
        }
        if (ItemKit.isBind(is)) {
            throw new CommandException("§c物品已绑定 请勿重复操作!");
        }
        return is;
    }

}
