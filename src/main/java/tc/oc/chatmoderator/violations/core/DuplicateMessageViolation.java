package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Duration;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;

/**
 * Violation called when a too many messages are being sent too fast.
 */
public class DuplicateMessageViolation extends Violation {

    /**
     * The time since the last message.
     */
    private final Duration timeSinceLast;

    /**
     * Instantiates a DuplicateMessageViolation.
     *
     * @param time The time that the violation occurred.
     * @param player The player that sent the message.
     * @param message The message that was sent.
     * @param timeSinceLast The instant since the last message.
     */
    public DuplicateMessageViolation(Instant time, OfflinePlayer player, String message, Duration timeSinceLast) {
        super(time, player, message, 1, true);

        this.timeSinceLast = Preconditions.checkNotNull(timeSinceLast);
    }

    /**
     * Gets the difference between the message and the last message that was sent by the player.
     *
     * @return The duration of time since the last message was sent.
     */
    public Duration getTimeSinceLast() {
        return this.timeSinceLast;
    }

}
