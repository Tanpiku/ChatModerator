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
import tc.oc.chatmoderator.listeners.managers.FilterManager;
import tc.oc.chatmoderator.listeners.managers.ZoneManager;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.Zone;
import tc.oc.chatmoderator.zones.ZoneType;

/**
 * Listener for chat-related events.
 */
public final class ChatModeratorListener implements Listener {
    private final ChatModeratorPlugin plugin;

    private final FilterManager filterManager;
    private final ZoneManager zoneManager;

    public ChatModeratorListener(final ChatModeratorPlugin plugin) {
        Preconditions.checkArgument(Preconditions.checkNotNull(plugin, "Plugin").isEnabled(), "Plugin not loaded.");
        this.plugin = plugin;

        this.filterManager = new FilterManager(this.plugin);
        this.zoneManager = new ZoneManager(this.plugin);
    }

    /**
     * Gets the filter manager.
     *
     * @return The filter manager.
     */
    public FilterManager getFilterManager() {
        return this.filterManager;
    }

    /**
     * Gets the zone manager.
     *
     * @return The zone manager.
     */
    public ZoneManager getZoneManager() {
        return this.zoneManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        Zone chatZone = this.getZoneManager().getZone(ZoneType.CHAT);

        if (!(chatZone.isEnabled())) {
            return;
        }

        String message = Preconditions.checkNotNull(event, "Event").getMessage();
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayer().getName());

        FixedMessage fixedMessage = new FixedMessage(message, Instant.now());

        for (Filter filter : this.getFilterManager().getFiltersForZone(chatZone)) {
            if (fixedMessage.getFixed() == null || fixedMessage.getFixed().equals("")) {
                break;
            }

            filter.filter(fixedMessage, player, ZoneType.CHAT, event);
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

        if (event.getMessage() == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSignEdit(SignChangeEvent event) {
        Zone signZone = this.getZoneManager().getZone(ZoneType.SIGN);

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

            if (event.getLine(i).equals("") || event.getLine(i) == null) {
                continue;
            }

            for (Filter filter : this.getFilterManager().getFiltersForZone(signZone)) {
                filter.filter(message, player, ZoneType.SIGN, event);
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
