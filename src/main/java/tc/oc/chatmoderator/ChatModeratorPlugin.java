package tc.oc.chatmoderator;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import tc.oc.chatmoderator.config.FilterConfiguration;
import tc.oc.chatmoderator.filters.core.*;
import tc.oc.chatmoderator.listeners.ChatModeratorListener;
import tc.oc.chatmoderator.listeners.DebugListener;

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
        this.configuration = null;
    }

    @Override
    public void onEnable() {
       
        // Set up configuration, copy defaults, etc etc
        this.saveDefaultConfig();
        this.reloadConfig();
        this.configuration = this.getConfig();

        // Set up the listeners and player manager
        this.listeners = new HashSet<>();
        this.playerManager = new PlayerManager(this);
        
        // Add debug options
        this.debugEnabled = this.configuration.getBoolean("debug.enabled", false);
        if (this.debugEnabled) {
            this.listeners.add(new DebugListener());
        }
        
        // Initialize the listener, add filters as necessary
        ChatModeratorListener moderatorListener = new ChatModeratorListener(this);
        setUpFilters(moderatorListener);
        
        this.listeners.add(moderatorListener);

        // And register all the events.
        for (Listener listener : this.listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    /**
     * Sets up all the filters in the CMListener.
     *
     * Lower priorities gets run first.
     */
    private void setUpFilters(ChatModeratorListener moderatorListener) {
        moderatorListener.registerFilter(new DuplicateMessageFilter(this.getPlayerManager(), new Permission("chatmoderator.filters.duplicatemessage.exempt"), getConfig().getLong("config.delay-between-messages"), getConfig().getInt("filters.duplicate-messages.priority")));
        moderatorListener.registerFilter(new IPFilter(this.getPlayerManager(), new Permission("chatmoderator.filters.ipfilter.exempt"), getConfig().getInt("filters.server-ip.priority")));
        moderatorListener.registerFilter(new ProfanityFilter(this.getPlayerManager(), new Permission("chatmoderator.filters.profanity.exempt"), (new FilterConfiguration(this, "filters.profanity.expressions")).build().getWeights(), getConfig().getInt("filters.profanity.priority")));
        moderatorListener.registerFilter(new AllCapsFilter(this.getPlayerManager(), new Permission("chatmoderator.filters.all-caps.exempt"), getConfig().getInt("filters.all-caps.priority")));
        moderatorListener.registerFilter(new RepeatedCharactersFilter(this.getPlayerManager(), new Permission("chatmoderator.filters.repeated.exempt"), getConfig().getInt("filters.repeated-characters.count"), getConfig().getInt("filters.repeated-characters.priority")));
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
