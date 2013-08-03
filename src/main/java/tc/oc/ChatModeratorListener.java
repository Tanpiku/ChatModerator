package tc.oc;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * Listener for chat-related events.
 */
public final class ChatModeratorListener implements Listener {
    @Nonnull
    private final ChatModeratorPlugin plugin;
    @Nonnull
    private final Set<Filter> filters = new HashSet<>();

    private ChatModeratorListener() {
        this.plugin = null;
    }

    ChatModeratorListener(@Nonnull final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(Preconditions.checkNotNull(plugin, "Plugin").isEnabled(), "Plugin not loaded.");
        this.plugin = plugin;
    }

    /**
     * Registers a filter. <b>Messages will be filtered in the order that they are registered in.</b>
     *
     * @param filter The filter to be registered.
     */
    void registerFilter(@Nonnull final Filter filter) {
        this.filters.add(Preconditions.checkNotNull(filter, "Filter"));
    }

    /**
     * Un-registers the specified filter.
     *
     * @param filter The filter to be unregistered.
     */
    void unregisterFilter(@Nonnull final Filter filter) {
        this.filters.remove(Preconditions.checkNotNull(filter, "Filter"));
    }

    /**
     * Un-registers all filters.
     */
    void unregisterAll() {
        for (Filter filter : this.filters) {
            this.filters.remove(filter);
        }
    }

    /**
     * Un-registers all filters of the specified type.
     *
     * @param type The type of filter to be unregistered.
     */
    void unregisterFilters(@Nonnull final Class<? extends Filter> type) {
        Preconditions.checkNotNull(type, "Type");
        for (Filter filter : this.filters) {
            if (filter.getClass().equals(type)) {
                this.filters.remove(filter);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(@Nonnull final AsyncPlayerChatEvent event) {
        String message = Preconditions.checkNotNull(event, "Event").getMessage();
        Player player = event.getPlayer();
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
