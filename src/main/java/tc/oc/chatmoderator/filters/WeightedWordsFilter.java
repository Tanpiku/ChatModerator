package tc.oc.chatmoderator.filters;

import org.bukkit.OfflinePlayer;
import org.bukkit.permissions.Permission;
import tc.oc.chatmoderator.PlayerManager;
import tc.oc.chatmoderator.messages.FixedMessage;
import tc.oc.chatmoderator.words.WordSet;
import tc.oc.chatmoderator.words.factories.WordSetFactory;
import tc.oc.chatmoderator.zones.ZoneType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class WeightedWordsFilter extends WeightedFilter {

    private boolean useFixed;

    /**
     * Represents the instantiable version of the WeightedFilter.
     *
     * @param playerManager    The base player manager.
     * @param exemptPermission The permission that will exempt you from the filter.
     * @param weights
     * @param priority
     */
    protected WeightedWordsFilter(PlayerManager playerManager, Permission exemptPermission, HashMap<Pattern, Double> weights, int priority, boolean useFixed) {
        super(playerManager, exemptPermission, weights, priority);

        this.useFixed = useFixed;
    }

    @Nullable
    @Override
    public abstract FixedMessage filter(FixedMessage message, OfflinePlayer player, ZoneType type);

    protected WordSet makeWordSet(final FixedMessage message) {
        WordSetFactory factory = new WordSetFactory(message, this.useFixed);

        return factory.build().getWordSet();
    }
}
