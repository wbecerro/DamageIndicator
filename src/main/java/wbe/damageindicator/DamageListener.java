package wbe.damageindicator;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Random;

public class DamageListener implements Listener {
    private DamageIndicator plugin;

    private FileConfiguration config;

    public DamageListener(DamageIndicator plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void showDamageDealt(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if(!(damager instanceof Player)) {
            return;
        }

        Player player = (Player) damager;

        if(!player.hasPermission("damageindicator.use")) {
            return;
        }

        String damage = String.valueOf((int) event.getFinalDamage());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(config.getString("damageMessage")
                .replace("%damage%", damage).replace("&", "§")));

        Random rand = new Random();

        Location loc = event.getEntity().getLocation().clone();
        loc.add(new Vector(.05 * (rand.nextBoolean() ? -1 : 0), 1.5f, .05 * (rand.nextBoolean() ? -1 : 0)));

        TextDisplay textDisplay = event.getEntity().getWorld().spawn(loc, TextDisplay.class);
        textDisplay.setText(ChatColor.RED + "❤ " + damage);
        textDisplay.setBillboard(Display.Billboard.VERTICAL);

        new BukkitRunnable() {
            @Override
            public void run() {
                textDisplay.remove();
            }
        }.runTaskLater(plugin, config.getInt("damageTime"));

    }
}
