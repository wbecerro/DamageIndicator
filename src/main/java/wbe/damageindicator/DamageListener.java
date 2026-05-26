package wbe.damageindicator;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.util.Random;

public class DamageListener implements Listener {
    private DamageIndicator plugin;

    private FileConfiguration config;

    public DamageListener(DamageIndicator plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void showDamageDealt(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) {
            return;
        }

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

    @EventHandler
    public void showDurabilityLeftOnDamage(PlayerItemDamageEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        if(!player.hasPermission("damageindicator.durability")) {
            return;
        }

        ItemStack item = event.getItem();
        long epoch;
        switch(item.getType().getEquipmentSlot()) {
            case HEAD:
                epoch = DamageIndicator.warnings.get(player).getHelmetLastWarning();
                break;
            case CHEST:
                epoch = DamageIndicator.warnings.get(player).getChestplateLastWarning();
                break;
            case LEGS:
                epoch = DamageIndicator.warnings.get(player).getLeggingsLastWarning();
                break;
            case FEET:
                epoch = DamageIndicator.warnings.get(player).getBootsLastWarning();
                break;
            default:
                epoch = DamageIndicator.warnings.get(player).getHandLastWarning();
                break;
        }

        if(Instant.now().getEpochSecond() - epoch < 3) {
            return;
        }

        Damageable itemMeta = (Damageable) item.getItemMeta();
        int maxDamage = item.getType().getMaxDurability();
        if(itemMeta.hasMaxDamage()) {
            maxDamage = itemMeta.getMaxDamage();
        }

        int damage = itemMeta.getDamage();
        int fifteen = (int) (maxDamage * 0.15);
        int ten = (int) (maxDamage * 0.1);
        int five = (int) (maxDamage * 0.05);
        int currentDurability = maxDamage - damage;

        String name = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : item.getType().toString().toLowerCase().replace("_", " ");
        String message = config.getString("lowDurability").replace("%item%", name)
                .replace("%uses%", String.valueOf(currentDurability))
                .replace("&", "§");

        boolean messaged = false;
        if(currentDurability <= five) {
            messaged = true;
            player.sendMessage(message);
            player.playSound(player, Sound.valueOf(config.getString("lowDurabilitySound")), 1f, 1f);
        } else if(currentDurability <= ten) {
            messaged = true;
            player.sendMessage(message);
            player.playSound(player, Sound.valueOf(config.getString("lowDurabilitySound")), 1f, 1f);
        } else if(currentDurability <= fifteen) {
            messaged = true;
            player.sendMessage(message);
        }

        if(messaged) {
            switch(item.getType().getEquipmentSlot()) {
                case HEAD:
                    DamageIndicator.warnings.get(player).setHelmetLastWarning(Instant.now().getEpochSecond());
                    break;
                case CHEST:
                    DamageIndicator.warnings.get(player).setChestplateLastWarning(Instant.now().getEpochSecond());
                    break;
                case LEGS:
                    DamageIndicator.warnings.get(player).setLeggingsLastWarning(Instant.now().getEpochSecond());
                    break;
                case FEET:
                    DamageIndicator.warnings.get(player).setBootsLastWarning(Instant.now().getEpochSecond());
                    break;
                default:
                    DamageIndicator.warnings.get(player).setHandLastWarning(Instant.now().getEpochSecond());
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void addPlayersOnJoin(PlayerJoinEvent event) {
        DamageIndicator.warnings.put(event.getPlayer(), new PlayerInfo(event.getPlayer()));
    }
}
