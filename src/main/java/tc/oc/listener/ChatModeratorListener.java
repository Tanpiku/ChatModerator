package tc.oc.listener;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tc.oc.ChatModeratorPlugin;
import tc.oc.Filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Listener for chat-related events.
 */
public final class ChatModeratorListener implements Listener {
    private final ChatModeratorPlugin plugin;
    private final Set<Filter> filters = new HashSet<>();

    private ChatModeratorListener() {
        this.plugin = null;
    }

    public ChatModeratorListener(final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(Preconditions.checkNotNull(plugin, "Plugin").isEnabled(), "Plugin not loaded.");
        this.plugin = plugin;
    }

    /**
     * Registers a filter. <b>Messages will be filtered in the order that they are registered in.</b>
     *
     * @param filter The filter to be registered.
     */
    public void registerFilter(final Filter filter) {
        this.filters.add(Preconditions.checkNotNull(filter, "Filter"));
    }

    /**
     * Un-registers the specified filter.
     *
     * @param filter The filter to be unregistered.
     */
    public void unRegisterFilter(final Filter filter) {
        this.filters.remove(Preconditions.checkNotNull(filter, "Filter"));
    }

    /**
     * Un-registers all filters.
     */
    public void unRegisterAll() {
        for (Filter filter : this.filters) {
            this.filters.remove(filter);
        }
    }

    /**
     * Un-registers all filters of the specified type.
     *
     * @param type The type of filter to be unregistered.
     */
    public void unRegisterFilters(final Class<? extends Filter> type) {
        Preconditions.checkNotNull(type, "Type");
        for (Filter filter : this.filters) {
            if (filter.getClass().equals(type)) {
                this.filters.remove(filter);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        String message = Preconditions.checkNotNull(event, "Event").getMessage();
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayer().getName());
        for (Filter filter : this.filters) {
            message = filter.filter(message, player);
        }
        if (message != null) {
            event.setMessage(message);
        } else {
            event.setCancelled(true);
        }
    }
}
