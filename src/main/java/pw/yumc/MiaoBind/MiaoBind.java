package pw.yumc.MiaoBind;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.config.Data;
import pw.yumc.MiaoBind.kit.ItemKit;
import pw.yumc.MiaoBind.listener.*;
import pw.yumc.MiaoBind.runnable.UpdateInventory;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.bukkit.P;
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

    public Config getMiaoBindConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        main = new CommandMain(new Command(config));
        new CommandSub("MiaoBind", this);
        new DeathListener(data);
        new ArmorStandListener();
        new PlayerListener();
        new InventoryListener();
        new SelfListener();
        try {
            Class.forName("org.bukkit.event.player.PlayerSwapHandItemsEvent");
            new SwapHandItemListener();
        } catch (Exception ignored) {
        }
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

    @Cmd(permission = "MiaoBind.default", executor = Cmd.Executor.PLAYER)
    @Help("领取保险柜物品")
    public void claim(final Player player) {
        data.claim(player);
        new UpdateInventory(player).runTask(P.instance);
    }

    @Cmd(permission = "MiaoBind.default")
    @Help("查看当前服务器Tag标签")
    public void tags(final CommandSender sender) {
        Log.sender(sender, "§6当前服务器使用的 §bTag标签 §6如下: ");
        Log.sender(sender, "§6绑定标签(§aBind§6): ");
        config.Tag.Bind.forEach(s -> Log.sender(sender, "- " + s));
        Log.sender(sender, "§6使用后绑定标签(§aBindOnUse§6): ");
        config.Tag.BindOnUse.forEach(s -> Log.sender(sender, "- " + s));
        Log.sender(sender, "§6装备后绑定标签(§aBindOnEquip§6): ");
        config.Tag.BindOnEquip.forEach(s -> Log.sender(sender, "- " + s));
        Log.sender(sender, "§6拾取后绑定标签(§aBindOnPickup§6): ");
        config.Tag.BindOnPickup.forEach(s -> Log.sender(sender, "- " + s));
    }
}
