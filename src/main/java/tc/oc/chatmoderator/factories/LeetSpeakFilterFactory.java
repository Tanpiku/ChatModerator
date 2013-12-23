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

public class LeetSpeakFilterFactory {

    private ChatModeratorPlugin plugin;

    private Map<Character, List<Pattern>> dictionary;

    private String path;

    public LeetSpeakFilterFactory(ChatModeratorPlugin plugin, String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin not enabled!");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        this.path = Preconditions.checkNotNull(path);

        this.dictionary = new HashMap<>();
    }

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

    public Map<Character, List<Pattern>> getDictionary() {
        return this.dictionary;
    }

    private String getPath() {
        return this.path;
    }
}
