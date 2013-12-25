package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.zones.ZoneType;

import java.util.ArrayList;
import java.util.List;

public class LeetSpeakViolation extends Violation {

    private List<String> words;

    /**
     * Public instantiable variant of the LeetSpeakViolation
     *
     * @param time The Instant that the message was sent at.
     * @param player The player that sent the offending message.
     * @param message The offending message that was sent.
     * @param level The severity of the violation.
     * @param zoneType The {@link tc.oc.chatmoderator.zones.ZoneType} where the violation occurred.
     */
    public LeetSpeakViolation(Instant time, OfflinePlayer player, String message, double level, ArrayList<String> words, ZoneType zoneType) {
        super(time, player, message, level, true, zoneType);

        this.words = Preconditions.checkNotNull(words);
    }

    public List<String> getWords() {
        return this.words;
    }
}
