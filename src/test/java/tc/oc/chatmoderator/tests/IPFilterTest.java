package tc.oc.chatmoderator.tests;

import junit.framework.TestCase;
import org.junit.Test;

import tc.oc.chatmoderator.filters.core.IPFilter;

import java.util.Random;

/**
 * Test for {@link tc.oc.chatmoderator.filters.core.IPFilter}.
 */
public final class IPFilterTest extends TestCase {
    private static final int TEST_ITERATIONS = 5;
    private Random random;

    @Override
    public void setUp() {
        this.random = new Random();
    }

    /**
     * Tests random IP addresses against the {@link IPFilter} regex to ensure that it works.
     */
    @Test
    public void testIPRegex() throws Exception {
        for (int i = 0; i < IPFilterTest.TEST_ITERATIONS; i++) {
            String ipAddress = this.random.nextInt(255) + "." + this.random.nextInt(255) + "." + this.random.nextInt(255) + "." + this.random.nextInt(255);
            assertTrue("testIPRegex() failed with IP address: [" + ipAddress + "]", IPFilter.getRegexPattern().matcher(ipAddress).matches());
        }
    }
}
