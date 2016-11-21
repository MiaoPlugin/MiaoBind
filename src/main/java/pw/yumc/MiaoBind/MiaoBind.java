package pw.yumc.MiaoBind;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.commands.CommandMain;
import pw.yumc.YumCore.commands.CommandSub;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.interfaces.Executor;

public class MiaoBind extends JavaPlugin implements Executor {
    private Config config;

    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    public Config getMiaoBindConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        new CommandMain(new Command());
        new CommandSub("MiaoBind", this);
    }

    @Override
    public void onLoad() {
        config = new Config();
        ItemKit.init(config);
    }

    @Cmd(permission = "MiaoBind.reload")
    @Help("重载配置文件")
    public void reload(final CommandSender sender) {
        config.reload();
        Log.sender(sender, "§a配置文件已重载!");
    }
}
