package pw.yumc.MiaoBind.config;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import pw.yumc.YumCore.config.inject.InjectConfigurationSection;

/**
 * @author MiaoWoo
 */
public class Tag extends InjectConfigurationSection {
    public List<String> Bind;
    public List<String> TimeBind;
    public List<String> BindOnPickup;
    public List<String> BindOnUse;
    public List<String> BindOnEquip;

    public Tag(ConfigurationSection config) {
        super(config);
    }
}
