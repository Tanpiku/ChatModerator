package tc.oc.chatmoderator.violations.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;

import tc.oc.chatmoderator.violations.Violation;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Added when a player posts a server IP in chat.
 */
public class ServerIPViolation extends Violation {
    private Set<InetAddress> ipAddresses = new HashSet<>();

    public ServerIPViolation(final Instant time, final OfflinePlayer player, final String message, final double level, final Set<InetAddress> ipAddresses) {
        super(time, player, message, level, true);
       
        for (InetAddress ipAddress : Preconditions.checkNotNull(ipAddresses, "IP addresses")) {
            this.ipAddresses.add(Preconditions.checkNotNull(ipAddress, "IP address"));
        }
    }

    /**
     * Gets the sent IP addresses.
     *
     * @return The sent IP addresses.
     */
    public Set<InetAddress> getIPAddresses() {
        return Collections.unmodifiableSet(this.ipAddresses);
    }
    
    /**
     * Sets the sent IP addresses.
     * 
     * @param ipAddresses The sent IP addresses
     */
    protected void setIPAddresses(Set<InetAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }
}
