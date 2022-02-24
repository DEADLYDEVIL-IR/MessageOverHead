package thedivazo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BubbleMessage {

    List<Bubble> bubbleMessages = new ArrayList<>();
    Location loc;
    BukkitTask[] tasksRunnable = null;
    Main plugin;

    public BubbleMessage(String message, Location loc, JavaPlugin plugin) {
        this.loc = loc;
        this.plugin = (Main) plugin;

        List<String> bubbleLines = new ArrayList<>();
        String[] messageLines = message.split(" ");

        StringBuilder bubbleLine = new StringBuilder("");
        int sizeColor = 0;
        String colorOld = "";

        for (String messageLine : messageLines) {
            String line = colorOld + messageLine;
            List<String> colors = new ArrayList<>();
            Matcher colorMatcher = Pattern.compile("" + ChatColor.COLOR_CHAR + "[0-9a-zA-Z]").matcher(line);
            while (colorMatcher.find()) {
                colors.add(colorMatcher.group());
            }

            sizeColor += colors.size() * 2;
            bubbleLine.append(line).append(" ");

            if ((bubbleLine.length() - sizeColor) > 24) {
                bubbleLines.add(bubbleLine.toString());
                bubbleLine.setLength(0);
                sizeColor = 0;
            }

            colorOld = String.join("", colors.toArray(new String[0]));
        }
        if (bubbleLine.length() - sizeColor * 2 != 0) {
            bubbleLines.add(colorOld + bubbleLine);
        }


        for (int i = 0; i < bubbleLines.size(); ++i) {
            Location locBubble = new Location(loc.getWorld(), loc.getX(), loc.getY() + i * 0.3D, loc.getZ());
            this.bubbleMessages.add(new Bubble(bubbleLines.get(bubbleLines.size() - 1 - i), locBubble));
        }

    }

    public void spawn(int distance) {
        for (Bubble msg : bubbleMessages) {
            msg.spawn(distance);
        }
    }

    public void spawn(Player player) {
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

    public void particle(int distance) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(loc) <= distance) {
                player.spawnParticle(plugin.particleType, loc,
                        plugin.particleCount,
                        plugin.particleOffsetX,
                        plugin.particleOffsetY,
                        plugin.particleOffsetZ);
            }
        }
    }

    public void removeTask(BukkitTask... tasksRunnable) {
        this.tasksRunnable = tasksRunnable;
    }

    public void sound(int distance) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(loc) <= distance) {
                player.playSound(loc,
                        plugin.soundType,
                        plugin.soundVolume,
                        plugin.soundPitch);
            }
        }
    }
}

