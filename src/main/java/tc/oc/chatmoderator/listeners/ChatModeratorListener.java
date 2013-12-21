package tc.oc.chatmoderator.listeners;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.joda.time.Instant;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.violations.Violation;

import java.util.*;

/**
 * Listener for chat-related events.
 */
public final class ChatModeratorListener implements Listener {
    private final ChatModeratorPlugin plugin;
    private List<Filter> filters = new ArrayList<>();

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
        plugin.getLogger().info("Registered filter: " + filter.getClass().getSimpleName());

        Collections.sort(this.filters);
    }

    /**
     * Un-registers the specified filter.
     *
     * @param filter The filter to be unregistered.
     */
    public void unRegisterFilter(final Filter filter) {
        this.filters.remove(Preconditions.checkNotNull(filter, "Filter"));
        plugin.getLogger().info("Unregistered filter: " + filter.getClass().getSimpleName());
    }

    /**
     * Un-registers all filters.
     */
    public void unRegisterAll() {
        for(Iterator<Filter> it = filters.iterator(); it.hasNext(); ) {
            Filter f = it.next();

            plugin.getLogger().info("Unregistered filter: " + f.getClass().getSimpleName());
            it.remove();
        }
    }

    /**
     * Un-registers all filters of the specified type.
     *
     * @param type The type of filter to be unregistered.
     */
    public void unRegisterFilters(final Class<? extends Filter> type) {
        Preconditions.checkNotNull(type, "Type");
        for(Iterator<Filter> it = filters.iterator(); it.hasNext(); ) {
            Filter f = it.next();

            if(f.getClass().equals(type)) {
                plugin.getLogger().info("Unregistered filter: " + f.getClass().getSimpleName());
                it.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        String message = Preconditions.checkNotNull(event, "Event").getMessage();
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayer().getName());

        FixedMessage fixedMessage = new FixedMessage(message, Instant.now());
        fixedMessage.setFixed(fixedMessage.getOriginal());

        for (Filter filter : this.filters) {
            filter.filter(fixedMessage, player);
        }

        event.setMessage(fixedMessage.getOriginal());

        for (Violation v : plugin.getPlayerManager().getViolationSet(player).getViolationsForTime(fixedMessage.getTimeSent())) {
            if (v.isCancelled()) {
                event.setMessage(null);
                event.setCancelled(true);
                break;
            }

            if (v.isFixed()) {
                event.setMessage(fixedMessage.getFixed());
            }
        }

        if (event.getMessage() == null)
            event.setCancelled(true);
    }
}
