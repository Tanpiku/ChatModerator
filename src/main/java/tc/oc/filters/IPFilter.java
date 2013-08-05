package tc.oc.filters;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.joda.time.Instant;
import tc.oc.ChatModeratorPlugin;
import tc.oc.Filter;
import tc.oc.Violation;
import tc.oc.ViolationSet;
import tc.oc.event.ViolationAddEvent;
import tc.oc.violations.ServerIPViolation;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to check for IP addresses in messages.
 */
public class IPFilter extends Filter {
    /**
     * <a href="http://stackoverflow.com/a/106223/2189153">Source</a>. TODO: Add better detection
     */
    public static final String IP_ADDRESS_EXPRESSION = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    private final Pattern regexPattern = Pattern.compile(IPFilter.IP_ADDRESS_EXPRESSION);

    public IPFilter(ChatModeratorPlugin plugin) {
        super(plugin);
    }

    /**
     * Filters a message. When this happens, violations are dispatched if necessary, and listeners of the violations can
     * modify the message.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     */
    @Nullable
    @Override
    public String filter(String message, final OfflinePlayer player) {
        Matcher matcher = this.regexPattern.matcher(Preconditions.checkNotNull(message));
        Set<InetAddress> ipAddresses = new HashSet<>();
        while (matcher.matches()) {
            try {
                ipAddresses.add(InetAddress.getByName(message.substring(matcher.start(), matcher.end())));
            } catch (UnknownHostException exception) {
                //  We'll never be here (yet); this is only thrown if an IP address can't be resolved from the given
                //  hostname, and we're currently only matching numerical IP addresses (though we should be matching
                //  hostnames, too, but the majority of IP spam is numerical).
                exception.printStackTrace();
            }
        }

        if (ipAddresses.size() > 0) {
            ViolationSet violations = this.plugin.getPlayerManager().getViolationSet(Preconditions.checkNotNull(player, "Player"));
            //  TODO: Think about this violation level formula. Currently taking number of IP violations and adding 1, dividing the sum by 2, multiplying the quotient by 5, adding the product to existing IP violation level.
            Violation violation = new ServerIPViolation(Instant.now(), player, message, violations.getViolationLevel(ServerIPViolation.class) + (((violations.getViolations(ServerIPViolation.class).size() + 1) / 2.0D) * 5.0D), (InetAddress[]) ipAddresses.toArray());
            violations.addViolation(violation);
            Bukkit.getPluginManager().callEvent(new ViolationAddEvent(player, violation));
            if (!violation.isCancelled()) {
                if (violation.isFixed()) {
                    message = violation.getMessage();
                }
            } else {
                message = null;
            }
        }

        return message;
    }
}
