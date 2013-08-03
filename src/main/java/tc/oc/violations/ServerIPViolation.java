package tc.oc.violations;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.joda.time.Instant;
import tc.oc.Violation;
import tc.oc.filters.IPFilter;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Added when a player posts a server IP in chat.
 */
public class ServerIPViolation extends Violation {
    @Nonnull
    private final Set<InetAddress> ipAddresses = new HashSet<>();
    @Nonnull
    private final String initialMessage;

    private ServerIPViolation() {
        super(null, null, null, 0.0D);
        this.initialMessage = null;
    }

    public ServerIPViolation(@Nonnull final Instant time, @Nonnull final Player player, @Nonnull final String message, final double level, @Nonnull final InetAddress... ipAddresses) {
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
    @Nonnull
    public Set<InetAddress> getIPAddresses() {
        return this.ipAddresses;
    }
}
