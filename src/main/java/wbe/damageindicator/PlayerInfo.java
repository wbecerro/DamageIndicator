package wbe.damageindicator;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInfo {

    private Player player;

    private long helmetLastWarning = 0L;

    private long chestplateLastWarning = 0L;

    private long leggingsLastWarning = 0L;

    private long bootsLastWarning = 0L;

    private long handLastWarning = 0L;

    public PlayerInfo(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getHelmetLastWarning() {
        return helmetLastWarning;
    }

    public void setHelmetLastWarning(long helmetLastWarning) {
        this.helmetLastWarning = helmetLastWarning;
    }

    public long getChestplateLastWarning() {
        return chestplateLastWarning;
    }

    public void setChestplateLastWarning(long chestplateLastWarning) {
        this.chestplateLastWarning = chestplateLastWarning;
    }

    public long getLeggingsLastWarning() {
        return leggingsLastWarning;
    }

    public void setLeggingsLastWarning(long leggingsLastWarning) {
        this.leggingsLastWarning = leggingsLastWarning;
    }

    public long getBootsLastWarning() {
        return bootsLastWarning;
    }

    public void setBootsLastWarning(long bootsLastWarning) {
        this.bootsLastWarning = bootsLastWarning;
    }

    public long getHandLastWarning() {
        return handLastWarning;
    }

    public void setHandLastWarning(long handLastWarning) {
        this.handLastWarning = handLastWarning;
    }
}
