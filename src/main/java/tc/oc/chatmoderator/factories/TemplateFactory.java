package tc.oc.chatmoderator.factories;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import tc.oc.chatmoderator.ChatModeratorPlugin;
import tc.oc.chatmoderator.template.Template;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Factory class for building a set of {@link tc.oc.chatmoderator.template.Template}.
 */
public class TemplateFactory {

    /**
     * The base plugin.
     */
    private final ChatModeratorPlugin plugin;

    /**
     * The path to search on.
     */
    private String path;

    /**
     * The generated list of weights.
     */
    private HashMap<Pattern, Double> weights;

    /**
     * Creates a {@link tc.oc.chatmoderator.factories.TemplateFactory} object.
     *
     * @param plugin The base plugin.
     * @param path The path to search on.
     */
    public TemplateFactory(final ChatModeratorPlugin plugin, String path) {
        Preconditions.checkArgument(plugin.isEnabled(), "plugin not enabled!");

        this.plugin = Preconditions.checkNotNull(plugin, "plugin");
        this.path = Preconditions.checkNotNull(path, "path");

        this.weights = new HashMap<>();
    }

    /**
     * Preforms all logic necessary to build a list of weights.
     *
     * @return The current state of the factory.
     */
    public TemplateFactory build() {
        ConfigurationSection words = this.plugin.getConfig().getConfigurationSection(path + ".words");

        for(Map.Entry<String, Object> entry : words.getValues(false).entrySet()) {
            MemorySection word = (MemorySection) entry.getValue();

            String templateBase = null;

            if(word.getString("template") == null) {
                if (plugin.isDebugEnabled()) {
                    plugin.getLogger().info("Searching for default template for word: " + entry.getKey());
                }

                templateBase = this.getDefaultTemplateBase();
            } else {
                String templateName = word.getString("template");
                templateBase = words.getParent().getString("templates." + templateName + ".expression");
            }

            String wordName = entry.getKey();
            Double level = ((MemorySection) entry.getValue()).getDouble("level");

            Pattern pattern = new Template(templateBase, wordName).build().getPattern();
            this.weights.put(pattern, level);

            if (plugin.isDebugEnabled()) {
                plugin.getLogger().info(pattern.toString() + " -- " + level);
            }
        }

        return this;
    }

    /**
     * Gets the generated set of weights.
     *
     * @return The weights.
     */
    public HashMap<Pattern, Double> getWeights() {
        return this.weights;
    }

    /**
     * Gets the default template for a section.
     *
     * @return The default template, if there is one.
     */
    private String getDefaultTemplateBase() {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path + ".templates");

        for(Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
            if(((MemorySection)entry.getValue()).getBoolean("default")) {
                return ((MemorySection) entry.getValue()).getString("expression");
            }
        }

        return null;
    }
}
