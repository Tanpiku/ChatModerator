package tc.oc.chatmoderator.util;

import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.filters.core.*;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple key-value store to map a short-name to the class representing a filter or violation.
 */
public class ChatModeratorUtil {

    /**
     * The key-value store for short-names to filter classes.
     */
    public static final Map<String, Class<? extends Filter>> filters = new HashMap<>();

    /**
     * The key-value store for short-names to violations.
     */
    public static final Map<String, Class<? extends Violation>> violations = new HashMap<>();

    /**
     * Sets up both the key-value stores.
     */
    static {
        filters.put("server-ip", IPFilter.class);
        filters.put("duplicate-messages", DuplicateMessageFilter.class);
        filters.put("repeated-characters", RepeatedCharactersFilter.class);
        filters.put("all-caps", AllCapsFilter.class);
        filters.put("profanity", ProfanityFilter.class);
        filters.put("leet", LeetSpeakFilter.class);

        violations.put("server-ip", ServerIPViolation.class);
        violations.put("duplicate-messages", DuplicateMessageViolation.class);
        violations.put("repeated-characters", RepeatedCharactersViolation.class);
        violations.put("all-caps", AllCapsViolation.class);
        violations.put("profanity", ProfanityViolation.class);
        violations.put("leet", LeetSpeakViolation.class);
    }
}
