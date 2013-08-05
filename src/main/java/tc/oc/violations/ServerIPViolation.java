package tc.oc.violations;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.Violation;
import tc.oc.filters.IPFilter;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Added when a player posts a server IP in chat.
 */
public class ServerIPViolation extends Violation {
    private final Set<InetAddress> ipAddresses = new HashSet<>();
    private final String initialMessage;

    private ServerIPViolation() {
        super(null, null, null, 0.0D);
        this.initialMessage = null;
    }

    public ServerIPViolation(final Instant time, final OfflinePlayer player, final String message, final double level, final InetAddress... ipAddresses) {
        super(time, player, message, level);
        this.initialMessage = message;
        for (InetAddress ipAddress : Preconditions.checkNotNull(ipAddresses, "IP addresses")) {
            this.ipAddresses.add(Preconditions.checkNotNull(ipAddress, "IP address"));
        }
    }

    /**
     * Sets whether or not the violating chat message should be fixed.
     *
     * @param fixed Whether or not the violating chat message should be fixed.
     */
    @Override
    public void setFixed(boolean fixed) {
        this.message = (fixed) ? this.initialMessage.replaceAll(IPFilter.IP_ADDRESS_EXPRESSION, "") : this.initialMessage;
        this.fixed = fixed;
    }

    /**
     * Gets the sent IP addresses.
     *
     * @return The sent IP addresses.
     */
    public Set<InetAddress> getIPAddresses() {
        return Collections.unmodifiableSet(this.ipAddresses);
    }
}
