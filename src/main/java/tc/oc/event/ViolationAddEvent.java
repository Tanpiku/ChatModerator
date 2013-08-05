package tc.oc.event;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import tc.oc.Violation;

import javax.annotation.Nonnull;

/**
 * Called when a violation is added to a player.
 */
public class ViolationAddEvent extends Event {
    /**
     * The handlers for the event.
     */
    private static final HandlerList handlers = new HandlerList();
    @Nonnull
    private final Violation violation;
    @Nonnull
    private final OfflinePlayer player;

    private ViolationAddEvent() {
        this.violation = null;
        this.player = null;
    }

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
