package pw.yumc.MiaoBind.config;

import java.text.SimpleDateFormat;
import java.util.List;

import pw.yumc.YumCore.config.annotation.ConfigNode;
import pw.yumc.YumCore.config.annotation.ReadOnly;
import pw.yumc.YumCore.config.inject.InjectConfig;

/**
 * @author MiaoWoo
 */
public class Config extends InjectConfig {
    @ConfigNode("Style.HideName")
    public Boolean HideName;
    @ReadOnly
    @ConfigNode("Style.DateFormat")
    public SimpleDateFormat DateFormat;
    @ReadOnly
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
}
