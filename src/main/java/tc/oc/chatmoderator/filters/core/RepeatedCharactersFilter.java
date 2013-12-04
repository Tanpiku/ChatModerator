package tc.oc.chatmoderator.filters.core;

import com.google.common.base.Preconditions;
import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;
import org.joda.time.Instant;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.PlayerViolationManager;
import tc.oc.chatmoderator.filters.Filter;
import tc.oc.chatmoderator.violations.Violation;
import tc.oc.chatmoderator.violations.core.RepeatedCharactersViolation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepeatedCharactersFilter extends Filter {

    private final Pattern pattern;
    private final int characterCount;

    public RepeatedCharactersFilter(PlayerManager playerManager, Permission exemptPermission, final int characterCount, int priority) {
        super(playerManager, exemptPermission, priority);

        Preconditions.checkArgument(characterCount > 1, "Character count must be greater than 1.");
        this.characterCount = characterCount;

        StringBuilder builder = new StringBuilder();
        builder.append("(.)\\1{");
        builder.append(this.characterCount);
        builder.append(",}");

        this.pattern = Pattern.compile(builder.toString());
    }

    /**
     * Called when a player sends a message in chat.  Searches for repeated characters greater or equal to a certain length.
     *
     * @param message The message that should be instead sent. This may be a modified message, the unchanged message, or
     *                <code>null</code>, if the message is to be cancelled.
     * @param player  The player that sent the message.
     *
     * @return If "fixed", the fixed message, else, the original message.
     */
    @Nullable
    @Override
    public String filter(String message, OfflinePlayer player) {
        Matcher matcher = this.pattern.matcher(message);
        List<String> repeatedCharacters = new ArrayList<>();

        PlayerViolationManager violationManager = this.getPlayerManager().getViolationSet(Preconditions.checkNotNull(player, "player"));
        Violation violation = new RepeatedCharactersViolation(Instant.now(), player, message, violationManager.getViolationLevel(RepeatedCharactersViolation.class), repeatedCharacters);

        while(matcher.find()) {
            repeatedCharacters.add(matcher.group());

            if(violation.isFixed()) {
                message = message.replaceFirst(matcher.group(), matcher.group().charAt(0) + "");
            }
        }

        if(repeatedCharacters.size() > 0) {
            violationManager.addViolation(violation);
        }

        return message;
    }

}
