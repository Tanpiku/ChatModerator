package tc.oc.chatmoderator.util;

import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.filters.core.*;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.*;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;

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
     * The key-value store for short-names to violations.
     */
    public static final Map<Class<? extends Filter>, Permission> permissions = new HashMap<>();

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

        permissions.put(AllCapsFilter.class, new Permission("chatmoderator.filter.allcaps.exempt"));
        permissions.put(DuplicateMessageFilter.class, new Permission("chatmoderator.filter.duplicatemessage.exempt"));
        permissions.put(IPFilter.class, new Permission("chatmoderator.filter.ipfilter.exempt"));
        permissions.put(RepeatedCharactersFilter.class, new Permission("chatmoderator.filter.repeatedcharacter.exempt"));
        permissions.put(ProfanityFilter.class, new Permission("chatmoderator.filter.profanity.exempt"));
        permissions.put(LeetSpeakFilter.class, new Permission("chatmoderator.filter.leetspeak.exempt"));
    }
}
