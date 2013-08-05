package tc.oc;

import junit.framework.TestCase;
import org.junit.Test;

import tc.oc.filters.IPFilter;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Test for {@link tc.oc.filters.IPFilter}.
 */
public final class IPFilterTest extends TestCase {
    private static final int TEST_ITERATIONS = 5;
    private Random random;
    private Pattern regexPattern;

    @Override
    public void setUp() {
        this.random = new Random();
        this.regexPattern = Pattern.compile(IPFilter.IP_ADDRESS_EXPRESSION);
    }

    /**
     * Tests random IP addresses against the {@link IPFilter} regex to ensure that it works.
     */
    @Test
    public void testIPRegex() throws Exception {
        for (int i = 0; i < IPFilterTest.TEST_ITERATIONS; i++) {
            String ipAddress = this.random.nextInt(255) + "." + this.random.nextInt(255) + "." + this.random.nextInt(255) + "." + this.random.nextInt(255);
            assertTrue("testIPRegex() failed with IP address: [" + ipAddress + "]", this.regexPattern.matcher(ipAddress).matches());
        }
    }
}
