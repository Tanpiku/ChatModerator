package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.chatmoderator.violations.Violation;

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
     */
    public LeetSpeakViolation(Instant time, OfflinePlayer player, String message, double level, ArrayList<String> words) {
        super(time, player, message, level, true);

        this.words = Preconditions.checkNotNull(words);
    }

    public List<String> getWords() {
        return this.words;
    }
}
