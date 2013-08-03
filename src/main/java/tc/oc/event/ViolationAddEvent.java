package tc.oc.event;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import tc.oc.Violation;

import javax.annotation.Nonnull;

/**
 * Called when a violation is added to a player.
 */
public class ViolationAddEvent extends PlayerEvent {
    /**
     * The handlers for the event.
     */
    private static final HandlerList handlers = new HandlerList();
    @Nonnull
    private final Violation violation;

    public ViolationAddEvent(@Nonnull final Player player, @Nonnull final Violation violation) {
        super(player);
        this.violation = Preconditions.checkNotNull(violation, "Violation");
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
