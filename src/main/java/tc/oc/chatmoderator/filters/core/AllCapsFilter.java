package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;
import org.joda.time.Instant;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.AllCapsViolation;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters out and dispatches violations on messages that have part or all containing all capital letters.
 */
public class AllCapsFilter extends Filter {

    private final Pattern pattern = Pattern.compile("[A-Z0-9, ]{2,}");

    /**
     * Publicly insatiable version of the AllCapsFilter.
     *
     * @param playerManager The base player manager.
     * @param exemptPermission The permission that exempts a player from the AllCapsFilter.
     */
    public AllCapsFilter(PlayerManager playerManager, Permission exemptPermission, int priority) {
        super(playerManager, exemptPermission, priority);
    }

    @Override
    public @Nullable String filter(String message, OfflinePlayer player) {
        Matcher matcher = this.pattern.matcher(Preconditions.checkNotNull(message, "message"));
        Set<String> upperCaseWords = new HashSet<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(player);
        Violation violation = new AllCapsViolation(Instant.now(), player, message, violationManager.getViolationLevel(AllCapsViolation.class), upperCaseWords);

        while (matcher.find()) {
            upperCaseWords.add(matcher.group().trim());

            if (violation.isFixed()) {
                message = message.replaceFirst(matcher.group().trim(), matcher.group().trim().toLowerCase());
            }
        }

        if (upperCaseWords.size() > 0) {
            violationManager.addViolation(violation);
        }

        return message;
    }
}
