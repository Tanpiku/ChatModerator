package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import com.google.common.net.InetAddresses;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;

import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.util.FixStyleApplicant.FixStyle;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.ServerIPViolation;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to check for IP addresses in messages.
 */
public class IPFilter extends Filter {

    // TODO: fix me!
    private static final Pattern pattern = Pattern.compile("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");

    public IPFilter(PlayerManager playerManager, Permission exemptPermission, int priority, FixStyleApplicant.FixStyle fixStyle) {
        super(playerManager, exemptPermission, priority, fixStyle);
    }

    /**
     * Filters a message. When this happens, violations are dispatched if necessary, and listeners of the violations can
     * modify the message. If the player is exempt (via permissions) from this filter, the message that was passed in is
     * returned instead of the filter processing it.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Nullable
    @Override
    public FixedMessage filter(FixedMessage message, final OfflinePlayer player, ZoneType type, Event event) {
//      if(((Player) player).hasPermission(this.getExemptPermission()))
//          return message;
     
        Matcher matcher = pattern.matcher(Preconditions.checkNotNull(message.getFixed()));
        Set<InetAddress> ipAddresses = new HashSet<>();

        PlayerViolationManager violations = this.getPlayerManager().getViolationSet(Preconditions.checkNotNull(player, "Player"));
        Violation violation = new ServerIPViolation(message.getTimeSent(), player, message, violations.getViolationLevel(ServerIPViolation.class), ImmutableSet.copyOf(ipAddresses), type, FixStyleApplicant.FixStyle.DASH, event);
        
        while (matcher.find()) {
            try {
                ipAddresses.add(InetAddresses.forString(matcher.group()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            message.setFixed(message.getFixed().replaceFirst(matcher.group(), FixStyleApplicant.fixString(matcher.group(), FixStyle.NONE)));
        }

        if (ipAddresses.size() > 0) {
            violations.addViolation(violation);
        }

        return message;
    }

    /**
     * Get the pattern for IP filter
     *
     * @return The pattern.
     */
    public static Pattern getPattern() {
        return pattern;
    }
}
