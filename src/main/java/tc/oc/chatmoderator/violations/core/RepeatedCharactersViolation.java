package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;

import java.util.List;
import java.util.Map;

/**
 * Called when a player posts a message with a large amount of repeated characters.
 */
public class RepeatedCharactersViolation extends Violation {

    /**
     * The store of repeated characters in the offending message.
     */
    private List<String> repeatedCharacters;

    /**
     * Publicly instantiable variant of RepeatedCharactersViolation.
     *
     * @param time The time that the violation occurred.
     * @param player The player that sent the offending message.
     * @param message The offending message that was sent.
     * @param level The level that the violation carries.
     */
    public RepeatedCharactersViolation(Instant time, OfflinePlayer player, String message, double level, List<String> repeatedCharacters) {
        super(time, player, message, level, true);

        this.repeatedCharacters = Preconditions.checkNotNull(repeatedCharacters);
    }

    /**
     * Gets the list of repeated characters in a message.
     *
     * @return The list of repeated characters.
     */
    public List<String> getRepeatedCharacters() {
        return this.repeatedCharacters;
    }
}
