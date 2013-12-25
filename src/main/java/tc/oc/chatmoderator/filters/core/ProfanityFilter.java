package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.WeightedFilter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.ProfanityViolation;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WeightedFilter that filters out different levels of profanity.  Multiple patterns.
 */
public class ProfanityFilter extends WeightedFilter {

    /**
     * Publicly insatiable version of this class.
     *
     * @param playerManager The base player manager.
     * @param exemptPermission The permission that will exempt a player from the filter.
     * @param weights The patterns and weights to search on.
     */
    public ProfanityFilter(PlayerManager playerManager, Permission exemptPermission, HashMap<Pattern, Double> weights, int priority) {
        super(playerManager, exemptPermission, weights, priority);
    }

    /**
     * Filter implementation for ProfanityFilter.  Removes and fixes (if set) all offending language, dispatching violations as necessary.
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
    public FixedMessage filter(FixedMessage message, OfflinePlayer player, ZoneType type) {
        Matcher matcher;
        Set<String> profanities = new HashSet<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(player);

        for(Pattern pattern : this.getWeights().keySet()) {
            Violation violation = new ProfanityViolation(message.getTimeSent(), player, message.getOriginal(), profanities, type);
            matcher = pattern.matcher(Preconditions.checkNotNull(message.getFixed(), "message"));

            while (matcher.find()) {
                String currentGroup = matcher.group();

                profanities.add(matcher.group());

                StringBuilder builder = new StringBuilder();

                builder.append(matcher.group().charAt(0));
                builder.append(ChatColor.MAGIC + "");
                builder.append(matcher.group().substring(1,matcher.group().length() - 1));
                builder.append(ChatColor.RESET + "");
                builder.append(matcher.group().charAt(matcher.group().length() - 1));

                message.setFixed(message.getFixed().replaceFirst(currentGroup, builder.toString()));
            }

            // TODO: Properly set the level for this violation
            violation.setLevel(super.getWeightFor(pattern));

            if (profanities.size() > 0) {
                violationManager.addViolation(violation);
            }

            profanities.clear();

            matcher = null;
        }

        return message;
    }


}
