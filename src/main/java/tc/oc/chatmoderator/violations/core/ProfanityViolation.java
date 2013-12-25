package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.Set;

/**
 * Added when a player posts various profanity in the chat.
 */
public class ProfanityViolation extends Violation {

    /**
     * The set of profanities that the player posted in chat.
     */
    private Set<String> profanities;

    /**
     * Public instantiable variant of the ProfanityViolation
     *
     * @param time The Instant that the message was sent at.
     * @param player The player that sent the offending message.
     * @param message The offending message that was sent.
     * @param profanities The set of profanities that were sent in the chat.
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} where the violation occurred.
     */
    public ProfanityViolation(Instant time, OfflinePlayer player, String message, Set<String> profanities, ZoneType zoneType) {
        super(time, player, message, -1, true, zoneType);
        this.profanities = Preconditions.checkNotNull(profanities);
    }

    /**
     * Gets the profanities associated with this specific violation.
     *
     * @return The profanities mentioned in the chat.
     */
    public Set<String> getProfanities() {
        return this.profanities;
    }

}
