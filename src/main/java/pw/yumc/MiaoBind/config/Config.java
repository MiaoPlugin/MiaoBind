package pw.yumc.MiaoBind.config;

import pw.yumc.YumCore.config.annotation.ConfigNode;
import pw.yumc.YumCore.config.inject.InjectConfig;

import java.text.DateFormat;

public class Config extends InjectConfig {
    @ConfigNode("Style.HideName")
    public Boolean HideName;
    @ConfigNode("Style.DateFormat")
    public DateFormat DateFormat;
    public Tag Tag;
    @ConfigNode("Setting.KeepOnDeath")
    public Boolean KeepOnDeath;
    @ConfigNode("Setting.PreventDrop")
    public Boolean PreventDrop;
    @ConfigNode("Setting.DeleteOnDrop")
    public Boolean DeleteOnDrop;
}
