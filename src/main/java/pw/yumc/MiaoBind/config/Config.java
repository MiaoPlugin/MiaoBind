package pw.yumc.MiaoBind.config;

import org.bukkit.inventory.ItemStack;
import pw.yumc.YumCore.config.annotation.ConfigNode;
import pw.yumc.YumCore.config.inject.InjectConfig;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config extends InjectConfig {
    @ConfigNode("Style.HideName")
    public Boolean HideName;
    @ConfigNode("Style.DateFormat")
    public SimpleDateFormat DateFormat;
    public Tag Tag;
    @ConfigNode("Setting.KeepOnDeath")
    public Boolean KeepOnDeath;
    @ConfigNode("Setting.PreventDrop")
    public Boolean PreventDrop;
    @ConfigNode("Setting.DeleteOnDrop")
    public Boolean DeleteOnDrop;
    @ConfigNode("Setting.AllowStore")
    public Boolean AllowStore;
    @ConfigNode("CheckArmor.Command")
    public List<String> CheckArmorCommand;
    public transient Map<String, List<ItemStack>> bindItems = new HashMap<>();

}
