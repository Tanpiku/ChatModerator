package tc.oc.chatmoderator.events;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import tc.oc.chatmoderator.violations.Violation;

import javax.annotation.Nonnull;

/**
 * Called when a violation is added to a player.
 */
public class ViolationAddEvent extends Event {
    /**
     * The handlers for the event.
     */
    private static final HandlerList handlers = new HandlerList();

    /**
     * The violation that was added.
     */
    private @Nonnull final Violation violation;

    /**
     * The player that violated.
     */
    private @Nonnull final OfflinePlayer player;

    /**
     * Creates a {@link tc.oc.chatmoderator.events.ViolationAddEvent}.
     *
     * @param player The {@link org.bukkit.OfflinePlayer} that caused the violation.
     * @param violation The {@link tc.oc.chatmoderator.violations.Violation} that was broken.
     */
    public ViolationAddEvent(@Nonnull final OfflinePlayer player, @Nonnull final Violation violation) {
        super(true);
        this.violation = Preconditions.checkNotNull(violation, "Violation");
        this.player = Preconditions.checkNotNull(player, "Player");
        Preconditions.checkArgument(player.equals(violation.getPlayer()), "Provided player (" + player + ") does not equal Violation player (" + violation + ")");
    }

    /**
     * Gets the handlers for the event.
     *
     * @return The handlers for the event.
     */
    public static HandlerList getHandlerList() {
        return ViolationAddEvent.handlers;
    }

    /**
     * Gets the handlers for the event.
     *
     * @return The handlers for the event.
     */
    @Override
    public HandlerList getHandlers() {
        return ViolationAddEvent.handlers;
    }

    /**
     * Gets the violation.
     *
     * @return The violation.
     */
    @Nonnull
    public Violation getViolation() {
        return violation;
    }
}
