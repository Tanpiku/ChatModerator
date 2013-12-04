package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;

import java.util.Set;

/**
 * Violation called when a player writes a word or set of words in all-caps.
 */
public class AllCapsViolation extends Violation {

    private final ImmutableSet<String> upperCaseWords;

    /**
     * Publicly insatiable variant of the AllCapsViolation.
     *
     * @param time The time when the violation happened.
     * @param player The player that sent the message.
     * @param message The message that was sent.
     * @param upperCaseWords The Set of words that was in all uppercase.
     */
    public AllCapsViolation(Instant time, OfflinePlayer player, String message, double level, Set<String> upperCaseWords) {
        super(time, player, message, level, true);

        this.upperCaseWords = ImmutableSet.copyOf(Preconditions.checkNotNull(upperCaseWords));
    }


    /**
     * Gets the words that were uppercase.
     *
     * @return The uppercase words.
     */
    public Set<String> getUpperCaseWords() {
        return this.upperCaseWords;
    }
}
