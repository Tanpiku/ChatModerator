package tc.oc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.filters.IPFilter;
import tc.oc.listener.ChatModeratorListener;
import tc.oc.listener.DebugListener;

import java.util.HashSet;
import java.util.Set;

/**
 * Plugin class.
 */
public class ChatModeratorPlugin extends JavaPlugin {
    private boolean debugEnabled;
    private Set<Listener> listeners;
    private PlayerManager playerManager;
    private Configuration configuration;

    /**
     * Gets whether or not debug mode is enabled.
     *
     * @return Whether or not debug mode is enabled.
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    @Override
    public void onDisable() {
        for (Listener listener : this.listeners) {
            if (listener instanceof ChatModeratorListener) {
                ((ChatModeratorListener) listener).unRegisterAll();
            }
            HandlerList.unregisterAll(listener);
        }
        this.playerManager = null;
        this.saveConfig();
        this.configuration = null;
    }

    @Override
    public void onEnable() {
        this.listeners = new HashSet<>();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
        this.configuration = this.getConfig();
        this.debugEnabled = this.configuration.getBoolean("debug.enabled", false);
        this.playerManager = new PlayerManager(this);
        ChatModeratorListener moderatorListener = new ChatModeratorListener(this);
        moderatorListener.registerFilter(new IPFilter(this));
        this.listeners.add(moderatorListener);
        if (this.debugEnabled) {
            this.listeners.add(new DebugListener());
        }
        for (Listener listener : this.listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    /**
     * Gets the player manager.
     *
     * @return The player manager.
     */
    public final PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
