package tc.oc.chatmoderator.listeners;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.joda.time.Instant;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.Zone;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.*;

/**
 * Listener for chat-related events.
 */
public final class ChatModeratorListener implements Listener {
    private final ChatModeratorPlugin plugin;
    private List<Filter> filters = new ArrayList<>();
    private Map<ZoneType, Zone> zones = new HashMap<>();

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
     * Registers a zone.
     *
     * @param type The type of zone being registered.
     * @param zone The zone associated with that type.
     */
    public void registerZone(final ZoneType type, final Zone zone) {
        if (this.zones.containsKey(type))
            throw new IllegalArgumentException("ChatModeratorListener already contains this zone!");

        this.zones.put(type, zone);
        plugin.getLogger().info("Registered zone: " + type.name());
    }

    /**
     * Removes a single zone for a specific type.
     *
     * @param type The type of zone being removed.
     */
    public void unRegisterZone(final ZoneType type) {
        this.zones.remove(type);
        plugin.getLogger().info("Unregistered zone: " + type.name());
    }

    /**
     * Un-registers all zones.
     */
    public void unRegisterAllZones() {
        for(ZoneType t : this.zones.keySet()) {
            this.zones.remove(t);
            plugin.getLogger().info("Unregistered zone: " + t.name());
        }
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
    public void unRegisterAllFilters() {
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

    private List<Filter> getFiltersForZone(Zone zone) {
        List<Filter> filters = new ArrayList<>();

        for (Filter filter : this.filters) {
            if (!(zone.getExcludedFilters().contains(filter.getClass()))) {
                filters.add(filter);
            }
        }

        return filters;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        Zone chatZone = this.zones.get(ZoneType.CHAT);

        if (!(chatZone.isEnabled()))
            return;

        String message = Preconditions.checkNotNull(event, "Event").getMessage();
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayer().getName());

        FixedMessage fixedMessage = new FixedMessage(message, Instant.now());
        fixedMessage.setFixed(fixedMessage.getOriginal());

        for(Class<? extends Filter> klass : chatZone.getExcludedFilters()) {
            plugin.getLogger().info(klass.getSimpleName());
        }

        for (Filter filter : this.getFiltersForZone(chatZone)) {
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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSignEdit(SignChangeEvent event) {
        Zone signZone = this.zones.get(ZoneType.SIGN);

        if(!(signZone.isEnabled()))
            return;

        OfflinePlayer player;

        try {
            player = (OfflinePlayer) event.getPlayer();
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
        }

        Instant signCreateInstant;

        for (int i = 0; i < event.getLines().length; i++) {
            signCreateInstant = Instant.now();

            FixedMessage message = new FixedMessage(event.getLine(i), signCreateInstant);
            message.setFixed(message.getOriginal());

            if (event.getLine(i).equals("") || event.getLine(i) == null)
                continue;

            for (Filter filter : this.getFiltersForZone(signZone)) {
                filter.filter(message, player);
            }

            event.setLine(i, message.getOriginal());

            for (Violation v : plugin.getPlayerManager().getViolationSet(player).getViolationsForTime(signCreateInstant)) {
                if (v.isCancelled()) {
                    event.setLine(i, null);
                    event.setCancelled(true);
                    break;
                }

                if (v.isFixed()) {
                    event.setLine(i, message.getFixed());
                }
            }

        }
    }
}
