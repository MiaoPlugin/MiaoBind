package pw.yumc.MiaoBind.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import pw.yumc.MiaoBind.config.Config;
import pw.yumc.MiaoBind.event.BindItemEvent;
import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.bukkit.P;

/**
 * Created with IntelliJ IDEA
 *
 * @author MiaoWoo
 * Created on 2019/11/19 12:44.
 */
public class SelfListener implements Listener {
    private Config config = P.getInjectConfig();

    public SelfListener() {
        Bukkit.getPluginManager().registerEvents(this, P.instance);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBindEvent(BindItemEvent event) {
        Log.d("绑定物品：%s 物品数量：%s 多重绑定：%s", event.getItemStack(), event.getItemStack().getAmount(), config.AllowStack);
        if (event.getItemStack().getAmount() != 1 && !config.AllowStack) {
            event.setCancelReason("§4绑定失败! §c当前服务器不允许绑定多个物品 请单独绑定!");
            event.setCancelled(true);
        }
    }
}
