package pw.yumc.MiaoBind;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.config.Data;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.listener.ArmorStandListener;
import pw.yumc.MiaoBind.listener.DeathListener;
import pw.yumc.MiaoBind.listener.InventoryListener;
import pw.yumc.MiaoBind.listener.PlayerListener;
import pw.yumc.MiaoBind.listener.SwapHandItemListener;
import pw.yumc.MiaoBind.runnable.UpdateInventory;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.bukkit.P;
import pw.yumc.YumCore.commands.CommandMain;
import pw.yumc.YumCore.commands.CommandSub;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.interfaces.Executor;
import pw.yumc.YumCore.statistic.Statistics;
import pw.yumc.YumCore.update.SubscribeTask;

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
        try {
            Class.forName("org.bukkit.event.player.PlayerSwapHandItemsEvent");
            new SwapHandItemListener();
        } catch (Exception ignored) {
        }
        new Statistics();
        new SubscribeTask(true, SubscribeTask.UpdateType.MAVEN);
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
        main.onCommand(sender, null, "miaobind", new String[]{"?"});
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
        data.claim(player);
        new UpdateInventory(player).runTask(P.instance);
    }
}
