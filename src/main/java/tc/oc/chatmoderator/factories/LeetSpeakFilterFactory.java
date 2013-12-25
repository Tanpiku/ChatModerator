package tc.oc.chatmoderator.factories;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import tc.oc.chatmoderator.ChatModeratorPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Factory class for building the {@link tc.oc.chatmoderator.filters.core.LeetSpeakFilter}
 */
public class LeetSpeakFilterFactory {

    /**
     * The base plugin, used for accessing configuration.
     */
    private ChatModeratorPlugin plugin;

    /**
     * The key-value store of the characters of the alphabet to their possible "leet" alternatives.
     */
    private Map<Character, List<Pattern>> dictionary;

    /**
     * The path to search on in the config.
     */
    private String path;

    /**
     * Available constructor for creating the LeetSpeakFilterFactory object.
     *
     * @param plugin The base plugin.
     * @param path The path to search on in the config.
     */
    public LeetSpeakFilterFactory(ChatModeratorPlugin plugin, String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin not enabled!");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        this.path = Preconditions.checkNotNull(path);

        this.dictionary = new HashMap<>();
    }

    /**
     * Parses the config specified in the constructor.
     *
     * @return The state of the {@link tc.oc.chatmoderator.factories.LeetSpeakFilterFactory} object.
     */
    public LeetSpeakFilterFactory build() {
        ConfigurationSection dictionarySection = this.plugin.getConfig().getConfigurationSection(path);

        for(Map.Entry<String, Object> entry : dictionarySection.getValues(false).entrySet()) {
            Character reference = entry.getKey().charAt(0);
            ArrayList<Pattern> translations = new ArrayList<>();

            for (String s : (ArrayList<String>) entry.getValue()) {
                try {
                    translations.add(Pattern.compile(Pattern.quote(s)));
                } catch (PatternSyntaxException e) {
                    plugin.getLogger().info("Error parsing: " + reference.toString() + " - " + s);
                }
            }

            if (plugin.isDebugEnabled()) {
                plugin.getLogger().info(reference.toString() + " -- " + translations.toString());
            }

            dictionary.put(reference, translations);
        }

        return this;
    }

    /**
     * Gets the dictionary associated with this filter.  Typically called after {@code build()}.
     * @return
     */
    public Map<Character, List<Pattern>> getDictionary() {
        return this.dictionary;
    }

    /**
     * Gets the path to search on.
     *
     * @return The path.
     */
    private String getPath() {
        return this.path;
    }
}
