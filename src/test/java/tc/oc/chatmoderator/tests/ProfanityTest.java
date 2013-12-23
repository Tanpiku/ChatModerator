package tc.oc.chatmoderator.tests;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Test for {@link tc.oc.chatmoderator.filters.core.ProfanityFilter}
 */
public class ProfanityTest extends TestCase {

    /**
     * Copy of the profanity that is provided in the default config.yml
     */
    private Map<Pattern, String> profanity;

    /**
     * Initialize the profanity HashMap, and add all the defaults (provided from config).
     */
    @Override
    public void setUp() {
        this.profanity = new HashMap<>();

        this.profanity.put(Pattern.compile("\\bf+uck+|f+uck+\\b"), "fuck");
        this.profanity.put(Pattern.compile("\\bw+ank+|w+ank+\\b"), "wank");
        this.profanity.put(Pattern.compile("\\bd+ick+|d+ick+\\b"), "dick");
        this.profanity.put(Pattern.compile("\\bc+ock+|c+ock+\\b"), "cock");
        this.profanity.put(Pattern.compile("\\bp+enis+|p+enis+\\b"), "penis");
        this.profanity.put(Pattern.compile("\\bs+hit+|s+hit+\\b"), "shit");
        this.profanity.put(Pattern.compile("\\bp+is+|p+is+\\b"), "piss");
        this.profanity.put(Pattern.compile("\\bs+keet+|s+keet+\\b"), "skeet");
        this.profanity.put(Pattern.compile("\\bb+itch+|b+itch+\\b"), "bitch");
        this.profanity.put(Pattern.compile("\\bt+its+|t+its+\\b"), "tits");
        this.profanity.put(Pattern.compile("\\bt+itties+|t+itties+\\b"), "titties");
        this.profanity.put(Pattern.compile("\\bp+ussy+|p+ussy+\\b"), "pussy");
        this.profanity.put(Pattern.compile("\\bc+unt+|c+unt+\\b"), "cunt");
        this.profanity.put(Pattern.compile("\\bt+wat+|t+wat+\\b"), "twat");
        this.profanity.put(Pattern.compile("\\bc+lit+|c+lit+\\b"), "clit");
        this.profanity.put(Pattern.compile("\\ba+ss+|a+ss+\\b"), "ass");
        this.profanity.put(Pattern.compile("\\bc+um+|c+um+\\b"), "cum");
        this.profanity.put(Pattern.compile("\\bn+igger+|n+igger+\\b"), "nigger");
        this.profanity.put(Pattern.compile("\\bf+aggot+|f+aggot+\\b"), "faggot");
        this.profanity.put(Pattern.compile("\\bq+ueer+|q+ueer+\\b"), "queer");
    }

    @Test
    public void testDefaultProfanity() throws Exception {
        for(Pattern pattern : this.profanity.keySet()) {
            String profanityMatch = this.profanity.get(pattern);

            String errorMessage = "testDefaultProfanity() failed with pattern [" + pattern.toString() + "] and word [" + profanityMatch +"]";

            assertTrue(errorMessage, pattern.matcher(profanityMatch).matches());
        }
    }
}
