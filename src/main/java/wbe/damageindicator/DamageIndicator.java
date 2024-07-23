package wbe.damageindicator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageIndicator extends JavaPlugin {

    private final DamageListener listeners = new DamageListener(this);

    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("CombatDummy enabled correctly.");
        this.getServer().getPluginManager().registerEvents(listeners, this);
    }

    @Override
    public void onDisable() {
        reloadConfig();
        getLogger().info("CombatDummy disabled correctly.");
    }
}
