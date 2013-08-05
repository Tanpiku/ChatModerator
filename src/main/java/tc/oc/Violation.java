package tc.oc;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;

/**
 * Represents an individual violation.
 */
public abstract class Violation {
    protected final Instant time;
    protected final OfflinePlayer player;
    //  TODO: Decimals or no?
    protected final double level;
    protected String message;
    protected boolean cancelled = false;
    protected boolean fixed = false;

    private Violation() {
        this.time = null;
        this.player = null;
        this.message = null;
        this.level = 0.0D;
    }

    /**
     * For subclasses only.
     */
    protected Violation(final Instant time, final OfflinePlayer player, final String message, final double level) {
        this.time = Preconditions.checkNotNull(time, "Time");
        this.player = Preconditions.checkNotNull(player, "Player");
        this.message = Preconditions.checkNotNull(message, "Message");
        Preconditions.checkArgument(level > 0.0D, "Level must be a positive integer and larger than 0.");
        this.level = level;
    }

    /**
     * Gets the time at which the message was sent.
     *
     * @return The time at which the message was sent.
     */
    public final Instant getTime() {
        return this.time;
    }

    /**
     * Gets the message.
     *
     * @return The message.
     */
    public final String getMessage() {
        return this.message;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public final OfflinePlayer getPlayer() {
        return this.player;
    }

    /**
     * Whether or not the violating chat message has been cancelled.
     *
     * @return Whether or not the violating chat message has been cancelled.
     */
    public final boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether or not the violating chat message should be cancelled.
     *
     * @param cancelled Whether or not the violating chat message should be cancelled.
     */
    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets whether or not the violating chat message has been fixed.
     *
     * @return Whether or not the violating chat message has been fixed.
     */
    public boolean isFixed() {
        return this.fixed;
    }

    /**
     * Sets whether or not the violating chat message should be fixed.
     *
     * @param fixed Whether or not the violating chat message should be fixed.
     */
    public void setFixed(boolean fixed) {
        this.setCancelled(fixed);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Violation{");
        sb.append("time=").append(time);
        sb.append(", player=").append(player);
        sb.append(", level=").append(level);
        sb.append(", message='").append(message).append('\'');
        sb.append(", cancelled=").append(cancelled);
        sb.append(", fixed=").append(fixed);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Gets the violation level.
     *
     * @return The violation level.
     */
    public final double getLevel() {
        return this.level;
    }
}
