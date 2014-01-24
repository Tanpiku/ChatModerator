package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.WeightedWordsFilter;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.messages.FixedWordMessage;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.util.MessageUtils;
import tc.oc.chatmoderator.util.PatternUtils;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.ProfanityViolation;
import tc.oc.chatmoderator.whitelist.Whitelist;
import tc.oc.chatmoderator.words.CorrectedWord;
import tc.oc.chatmoderator.words.Word;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WeightedFilter that filters out different levels of profanity.  Multiple patterns.
 */
public class ProfanityFilter extends WeightedWordsFilter {

    private FixStyleApplicant.FixStyle defaultFixStyle;

    /**
     * Publicly insatiable version of this class.
     *
     * @param playerManager The base player manager.
     * @param exemptPermission The permission that will exempt a player from the filter.
     * @param weights The patterns and weights to search on.
     */
    public ProfanityFilter(PlayerManager playerManager, Permission exemptPermission, HashMap<Pattern, Double> weights, int priority, Whitelist whitelist, FixStyleApplicant.FixStyle fixStyle) {
        super(playerManager, exemptPermission, weights, priority, true, whitelist, fixStyle);
        this.defaultFixStyle = Preconditions.checkNotNull(fixStyle);
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
    public FixedMessage filter(FixedMessage message, OfflinePlayer player, ZoneType type, Event event) {
        Matcher matcher;
        Set<String> profanities = new HashSet<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(player);

        FixedWordMessage wordMessage = new FixedWordMessage(message, Pattern.compile(PatternUtils.getSplitPattern()));

        List<CorrectedWord> wordList = MessageUtils.splitWithWeights(this.getWeights(), wordMessage.getFixedWordSet().toList());
        message.setFixed(MessageUtils.evaluate(wordList, wordMessage.getFixedWordSet()).toString());

        WordSet wordSet = this.makeWordSet(message);

        for (Word word : wordSet.toList()) {
            if (this.getWhitelist().containsWord(word, true)) {
                word.setChecked(true);
                continue;
            }

            for(Pattern pattern : this.getWeights().keySet()) {

                Violation violation = new ProfanityViolation(message.getTimeSent(), player, message, profanities, type, FixStyleApplicant.FixStyle.MAGIC, event);
                matcher = pattern.matcher(Preconditions.checkNotNull(word.getWord(), "word"));

                while (matcher.find()) {
                    profanities.add(matcher.group());

                    word.setWord(FixStyleApplicant.fixWord(word, violation.getFixStyle()).getWord());
                    message.setFixed(wordSet.toString());
                }

                // TODO: Properly set the level for this violation
                violation.setLevel(super.getWeightFor(pattern));

                if (profanities.size() > 0) {
                    violationManager.addViolation(violation);
                }

                profanities.clear();

                matcher = null;
            }

            word.setChecked(true);
        }

        return message;
    }
}
