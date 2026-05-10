package wbe.damageindicator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class DamageIndicator extends JavaPlugin {

    private final DamageListener damageListener = new DamageListener(this);

    private FileConfiguration config = getConfig();

    public static HashMap<Player, Long> warnings = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("DamageIndicator enabled correctly.");
        this.getServer().getPluginManager().registerEvents(damageListener, this);
    }

    @Override
    public void onDisable() {
        reloadConfig();
        getLogger().info("DamageIndicator disabled correctly.");
    }
}
