package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.List;

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
     * @param repeatedCharacters The selection of matches that didn't pass the {@link tc.oc.chatmoderator.filters.core.RepeatedCharactersFilter}
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} where the violation occurred.
     */
    public RepeatedCharactersViolation(Instant time, OfflinePlayer player, String message, double level, List<String> repeatedCharacters, ZoneType zoneType) {
        super(time, player, message, level, true, zoneType);

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
