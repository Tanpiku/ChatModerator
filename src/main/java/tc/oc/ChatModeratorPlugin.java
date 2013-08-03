package tc.oc;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import tc.oc.filters.IPFilter;

import javax.annotation.Nonnull;

/**
 * Plugin class.
 */
public class ChatModeratorPlugin extends JavaPlugin {
    @Nonnull
    private ChatModeratorListener listener;
    @Nonnull
    private PlayerManager playerManager;

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this.listener);
        this.listener.unregisterAll();
        this.playerManager = null;
    }

    @Override
    public void onEnable() {
        this.playerManager = new PlayerManager(this);
        this.listener = new ChatModeratorListener(this);
        this.listener.registerFilter(new IPFilter(this));
        Bukkit.getPluginManager().registerEvents(this.listener, this);

    }

    /**
     * Gets the player manager.
     *
     * @return The player manager.
     */
    @Nonnull
    public final PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
