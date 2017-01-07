package pw.yumc.MiaoBind.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pw.yumc.YumCore.bukkit.Log;
import pw.yumc.YumCore.config.inject.InjectConfig;

public class Data extends InjectConfig {
    public Map<String, List<ItemStack>> bindItems;
    public Map<String, List<ItemStack>> dropItems;

    public Data() {
        super("data.yml");
    }

    public void recover(Player player) {
        List<ItemStack> items = bindItems.remove(player.getName());
        if (items != null && !items.isEmpty()) {
            final HashMap<Integer, ItemStack> result = player.getInventory().addItem(items.toArray(new ItemStack[] {}));
            if (result != null && !result.isEmpty()) {
                dropItems.put(player.getName(), new ArrayList<>(result.values()));
                Log.sender(player, "§c由于您的背包已满 部分绑定物品已经由服务器保留 /mbind claim 领取物品!");
            }
        }
        save();
    }

    public void claim(Player player) {
        List<ItemStack> result = dropItems.remove(player.getName());
        if (result == null || result.isEmpty()) {
            Log.sender(player, "§c您没有被保留的物品 无法领取!");
            return;
        }
        HashMap<Integer, ItemStack> drop = player.getInventory().addItem(result.toArray(new ItemStack[] {}));
        if (drop != null && !drop.isEmpty()) {
            dropItems.put(player.getName(), new ArrayList<>(drop.values()));
            Log.sender(player, "§c由于您的背包已满 部分绑定物品已经由服务器保留 /mbind claim 领取物品!");
        } else {
            Log.sender(player, "§a已领取所有保留的物品!");
        }
        save();
    }

    @Override
    public synchronized void save() {
        super.save();
    }
}
