package pw.yumc.MiaoBind;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.commands.CommandManager;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.interfaces.Executor;

public class MiaoBind extends JavaPlugin implements Executor {
    private MiaoBindConfig config;

    @Override
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    public MiaoBindConfig getMiaoBindConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        new CommandManager("MiaoBind", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "MiaoBind");
    }

    @Override
    public void onLoad() {
        config = new MiaoBindConfig();
    }

    @Cmd(permission = "MiaoBind.reload")
    @Help("重载配置文件")
    public void reload(final CommandSender sender) {
        config.reload();
        Log.toSender(sender, "§a配置文件已重载!");
    }
}
