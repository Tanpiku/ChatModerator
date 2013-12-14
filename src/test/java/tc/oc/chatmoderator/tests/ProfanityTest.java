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

        this.profanity.put(Pattern.compile("f+[^a-zA-Z\\s]*u+[^a-zA-Z\\s]*[ck]*[^a-zA-Z\\s]*[ck]+"), "fuck");
        this.profanity.put(Pattern.compile("w+[^a-zA-Z\\s]*a+[^a-zA-Z\\s]*n+[^a-zA-Z\\s]*[ck]+"), "wank");
        this.profanity.put(Pattern.compile("d+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*c+[^a-zA-Z\\s]*[ck]+"), "dick");
        this.profanity.put(Pattern.compile("[ck]+[^a-zA-Z\\s]*o+[^a-zA-Z\\s]*[ck]+[^a-zA-Z\\s]*[ck]+"), "cock");
        this.profanity.put(Pattern.compile("p+[^a-zA-Z\\s]*e+[^a-zA-Z\\s]*n+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*s+"), "penis");
        this.profanity.put(Pattern.compile("s+[^a-zA-Z\\s]*h+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*[tz]+"), "shit");
        this.profanity.put(Pattern.compile("p+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*s+[^a-zA-Z\\s]*s+"), "piss");
        this.profanity.put(Pattern.compile("s+[^a-zA-Z\\s]*[ck]+[^a-zA-Z\\s]*e+[^a-zA-Z\\s]*e+[^a-zA-Z\\s]*t+"), "skeet");
        this.profanity.put(Pattern.compile("b+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*t+[^a-zA-Z\\s]*c+[^a-zA-Z\\s]*h+"), "bitch");
        this.profanity.put(Pattern.compile("t+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*t+[^a-zA-Z\\s]*s+"), "tits");
        this.profanity.put(Pattern.compile("t+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*t+[^a-zA-Z\\s]*t+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*e+[^a-zA-Z\\s]*s+"), "titties");
        this.profanity.put(Pattern.compile("p+[^a-zA-Z\\s]*u+[^a-zA-Z\\s]*s+[^a-zA-Z\\s]*s+[^a-zA-Z\\s]*[yie]+"), "pussy");
        this.profanity.put(Pattern.compile("[kc]+[^a-zA-Z\\s]*u+[^a-zA-Z\\s]*n+[^a-zA-Z\\s]*t+"), "cunt");
        this.profanity.put(Pattern.compile("t+[^a-zA-Z\\s]*w+[^a-zA-Z\\s]*a+[^a-zA-Z\\s]*t+"), "twat");
        this.profanity.put(Pattern.compile("[ck]+[^a-zA-Z\\s]*l+[^a-zA-Z\\s]*i+[^a-zA-Z\\s]*t+"), "clit");
        this.profanity.put(Pattern.compile("a+[^a-zA-Z\\s]*s+[^a-zA-Z\\s]*s+"), "ass");
        this.profanity.put(Pattern.compile("[ck]+[^a-zA-Z\\s]*u+[^a-zA-Z\\s]*m+"), "cum");
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
