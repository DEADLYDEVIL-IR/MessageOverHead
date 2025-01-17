package thedivazo;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import thedivazo.config.ConfigBubble;

import java.util.ArrayList;
import java.util.List;

public class BubbleMessage {

    private final List<Bubble> bubbleMessages = new ArrayList<>();
    private final Location loc;

    public ConfigBubble getConfigBubble() {
        return configBubble;
    }

    private ConfigBubble configBubble;
    private BukkitTask[] tasksRunnable = null;
    private final Player ownerPlayer;

    public BubbleMessage(Player player, Location loc, List<String> message, ConfigBubble configBubble) {
        this.configBubble = configBubble;
        ownerPlayer = player;
        this.loc = loc;
        for (int i = 0; i < message.size(); i++) {
            if (message.get(message.size() - 1 - i).length() > 0 && !message.get(message.size() - 1 - i).equals(" ")) {
                Location locBubble = new Location(loc.getWorld(), loc.getX(), loc.getY() + i * 0.1D, loc.getZ());
                this.bubbleMessages.add(new Bubble(message.get(message.size() - 1 - i), locBubble));
            }
        }
    }

    public Player getOwnerPlayer() {
        return ownerPlayer;
    }

    public void show(Player player) {
        for (Bubble msg : bubbleMessages) {
            msg.spawn(player);
        }
    }

    public void setPosition(Location position) {
        setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setPosition(double x, double y, double z) {
        for (int i = 0; i < bubbleMessages.size(); i++) {
            Location locBubble = new Location(loc.getWorld(), x, y + i * 0.3, z);
            bubbleMessages.get(i).setPosition(locBubble);
        }
    }

    public void remove() {
        if (tasksRunnable != null) {
            for (BukkitTask task : tasksRunnable) {
                task.cancel();
            }
        }

        bubbleMessages.forEach(Bubble::remove);
    }

    public void playParticle(Player player) {
        player.spawnParticle(configBubble.getParticleType(), loc,
                configBubble.getParticleCount(),
                configBubble.getParticleOffsetX(),
                configBubble.getParticleOffsetY(),
                configBubble.getParticleOffsetZ());
    }

    public void setTask(BukkitTask... tasksRunnable) {
        this.tasksRunnable = tasksRunnable;
    }

    public void playSound(Player player) {
        player.playSound(loc,
                configBubble.getSoundType(),
                configBubble.getSoundVolume(),
                configBubble.getSoundPitch());
    }
}

