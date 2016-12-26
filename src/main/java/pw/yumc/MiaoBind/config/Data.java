package pw.yumc.MiaoBind.config;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import pw.yumc.YumCore.config.annotation.Nullable;
import pw.yumc.YumCore.config.inject.InjectConfig;

public class Data extends InjectConfig {
    @Nullable
    public Map<String, List<ItemStack>> bindItems;
    @Nullable
    public Map<String, List<ItemStack>> dropItems;

    public Data() {
        super("data.yml");
    }

    @Override
    public synchronized void save() {
        super.save();
    }
}
