package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.WordFilter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.AllCapsViolation;
import tc.oc.chatmoderator.whitelist.Whitelist;
import tc.oc.chatmoderator.words.Word;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters out and dispatches violations on messages that have part or all containing all capital letters.
 */
public class AllCapsFilter extends WordFilter {

    private static final Pattern pattern = Pattern.compile("^[^a-z]*$");

    /**
     * Publicly insatiable version of the AllCapsFilter.
     *
     * @param playerManager The base player manager.
     * @param exemptPermission The permission that exempts a player from the AllCapsFilter.
     */
    public AllCapsFilter(PlayerManager playerManager, Permission exemptPermission, int priority, Whitelist whitelist) {
        super(playerManager, exemptPermission, priority, true, whitelist, FixStyleApplicant.FixStyle.NONE);
    }

    /**
     * Filters out messages written in all-caps
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     * @param type    The {@link tc.oc.chatmoderator.zones.ZoneType} relating to where the message originated from.
     *
     * @return The state of the message after running this filter.
     */
    @Override
    public @Nullable FixedMessage filter(FixedMessage message, OfflinePlayer player, ZoneType type) {
        WordSet wordSet = this.makeWordSet(message);

        Matcher matcher = null;
        Set<String> upperCaseWords = new HashSet<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(player);
        Violation violation = new AllCapsViolation(message.getTimeSent(), player, message.getOriginal(), violationManager.getViolationLevel(AllCapsViolation.class), upperCaseWords, type);

        boolean first = true;

        for (Word word : wordSet.toList()) {
            if (this.whitelist.containsWord(word, false)) {
                word.setChecked(true);
                continue;
            }

            matcher = AllCapsFilter.pattern.matcher(Preconditions.checkNotNull(word.getWord(), "word"));

            while (matcher.find()) {
                upperCaseWords.add(matcher.group().trim());

                word.setWord(word.getWord().replaceFirst(Pattern.quote(matcher.group()), matcher.group().toLowerCase()));
                message.setFixed(wordSet.toString());
            }

            if (upperCaseWords.size() > 0) {
                violationManager.addViolation(violation);
            }

            word.setChecked(true);
            upperCaseWords.clear();
        }

        return message;
    }

    /**
     * Get the pattern for all-caps.
     *
     * @return The pattern.
     */
    public static Pattern getPattern() {
        return AllCapsFilter.pattern;
    }
}
