package pw.yumc.MiaoBind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.config.Data;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.listener.ArmorStandListener;
import pw.yumc.MiaoBind.listener.DeathListener;
import pw.yumc.MiaoBind.listener.InventoryListener;
import pw.yumc.MiaoBind.listener.PlayerListener;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.commands.CommandMain;
import pw.yumc.YumCore.commands.CommandSub;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.interfaces.Executor;

public class MiaoBind extends JavaPlugin implements Executor {
    private Data data;
    private Config config;
    private CommandMain main;

    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    @Override
    public void onEnable() {
        main = new CommandMain(new Command(config));
        new CommandSub("MiaoBind", this);
        new DeathListener(data);
        new ArmorStandListener();
        new PlayerListener(config);
        new InventoryListener(config);
    }

    @Override
    public void onLoad() {
        data = new Data();
        config = new Config();
        ItemKit.init(config);
    }

    @Cmd(permission = "MiaoBind.admin", aliases = "?")
    @Help("查看全局帮助")
    public void allhelp(final CommandSender sender) {
        main.onCommand(sender, null, "miaobind", new String[] { "?" });
    }

    @Cmd(permission = "MiaoBind.reload")
    @Help("重载配置文件")
    public void reload(final CommandSender sender) {
        config.reload();
        Log.sender(sender, "§a配置文件已重载!");
    }

    @Cmd(permission = "MiaoBind.default")
    @Help("重载配置文件")
    public void claim(final Player player) {
        List<ItemStack> result = data.dropItems.remove(player.getName());
        if (result == null || result.isEmpty()) {
            Log.sender(player, "§c您没有被保留的物品 无法领取!");
            return;
        }
        HashMap<Integer, ItemStack> drop = player.getInventory().addItem(result.toArray(new ItemStack[] {}));
        if (drop != null && !drop.isEmpty()) {
            data.dropItems.put(player.getName(), new ArrayList<>(drop.values()));
            Log.sender(player, "§c由于您的背包已满 部分绑定物品已经由服务器保留 /mbind claim 领取物品!");
        }
        Log.sender(player, "§a配置文件已重载!");
    }
}
