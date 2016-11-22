package pw.yumc.MiaoBind;

import java.text.ParseException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.annotation.Option;
import pw.yumc.YumCore.commands.interfaces.Executor;

/**
 * 命令类
 * 
 * @since 2016/11/18 0018
 * @author 喵♂呜
 */
public class Command implements Executor {
    private Config Config;
    boolean newVersion = true;
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

    public ItemStack getItemInHand(Player player) {
        if (newVersion) {
            return player.getInventory().getItemInMainHand();
        } else {
            return player.getItemInHand();
        }
    }

    @Cmd(executor = Cmd.Executor.PLAYER)
    @Help("绑定物品")
    public void bind(Player player, @Option("check") Player target) {
        ItemStack is = getItemInHand(player);
        if (ItemKit.isBind(is)) {
            Log.sender(player, "§c物品已绑定 请勿重复操作!");
            return;
        }
        ItemKit.bindItem(target == null ? player : target, is);
        Log.sender(player, "§a物品绑定成功!");
    }

    @Cmd(minimumArguments = 1, executor = Cmd.Executor.PLAYER)
    @Help("绑定时限物品")
    public void bindtime(Player player, String name, String time) {
        Player target = player;
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
            Log.sender(player, "§c时间 §e%s §c格式不正确 应为 §e%s!", time, Config.DateFormat);
            return;
        }
        Log.sender(player, "§a物品时间绑定成功 将于 §e%s §a过期!", time);
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "ub")
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

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "bop")
    @Help("拾取时绑定物品")
    public void bindonpickup(Player player) {
        ItemStack is = getItemInHand(player);
        if (ItemKit.isBind(is)) {
            Log.sender(player, "§c物品已绑定 请勿重复操作!");
            return;
        }
        ItemKit.bopItem(is);
        Log.sender(player, "§a物品已设定为 拾取时绑定物品!");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "boe")
    @Help("装备时绑定物品")
    public void bindonequip(Player player) {
        ItemStack is = getItemInHand(player);
        if (ItemKit.isBind(is)) {
            Log.sender(player, "§c物品已绑定 请勿重复操作!");
            return;
        }
        ItemKit.boeItem(is);
        Log.sender(player, "§a物品已设定为 装备时绑定物品!");
    }

    @Cmd(executor = Cmd.Executor.PLAYER, aliases = "bou")
    @Help("使用时绑定物品")
    public void bindonuse(Player player) {
        ItemStack is = getItemInHand(player);
        if (ItemKit.isBind(is)) {
            Log.sender(player, "§c物品已绑定 请勿重复操作!");
            return;
        }
        ItemKit.bouItem(is);
        Log.sender(player, "§a物品已设定为 使用时绑定物品!");
    }
}
