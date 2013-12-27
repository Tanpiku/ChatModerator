package tc.oc.chatmoderator.filters;

import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.words.factories.WordSetFactory;

public abstract class WordFilter extends Filter {

    private boolean useFixed;

    protected WordFilter(PlayerManager playerManager, Permission exemptPermission, int priority, boolean useFixed) {
        super(playerManager, exemptPermission, priority);

        this.useFixed = useFixed;
    }

    protected WordSet makeWordSet(final FixedMessage message) {
        WordSetFactory factory = new WordSetFactory(message, this.useFixed);

        return factory.build().getWordSet();
    }
}
