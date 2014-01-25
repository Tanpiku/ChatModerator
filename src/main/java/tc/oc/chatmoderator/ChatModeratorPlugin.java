package tc.oc.chatmoderator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import tc.oc.chatmoderator.factories.core.LeetSpeakFilterFactory;
import tc.oc.chatmoderator.factories.core.TemplateFactory;
import tc.oc.chatmoderator.factories.core.ZoneFactory;
import tc.oc.chatmoderator.filters.core.*;
import tc.oc.chatmoderator.listeners.ChatModeratorListener;
import tc.oc.chatmoderator.listeners.DebugListener;
import tc.oc.chatmoderator.settings.Settings;
import tc.oc.chatmoderator.util.FixStyleApplicant;
import tc.oc.chatmoderator.whitelist.factories.WhitelistFactory;
import tc.oc.chatmoderator.zones.ZoneType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Plugin class.
 */
public class ChatModeratorPlugin extends JavaPlugin {
    private boolean debugEnabled;
    private Set<Listener> listeners;
    private PlayerManager playerManager;
    private Configuration configuration;

    /**
     * Gets whether or not debug mode is enabled.
     *
     * @return Whether or not debug mode is enabled.
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    @Override
    public void onDisable() {
        for (Listener listener : this.listeners) {
            if (listener instanceof ChatModeratorListener) {
                ((ChatModeratorListener) listener).getZoneManager().unRegisterAllZones();
                ((ChatModeratorListener) listener).getFilterManager().unRegisterAllFilters();
            }
            HandlerList.unregisterAll(listener);
        }
        this.playerManager = null;
        this.configuration = null;
    }

    @Override
    public void onEnable() {
       
        // Set up configuration, copy defaults, etc etc
        this.saveDefaultConfig();
        this.reloadConfig();
        this.configuration = this.getConfig();
        this.saveResource(new File("dictionary.yml").getPath(), false);

        // Set up the listeners and player manager
        this.listeners = new HashSet<>();
        this.playerManager = new PlayerManager(this);
        
        // Add debug options
        this.debugEnabled = this.configuration.getBoolean("debug.enabled", false);
        if (this.debugEnabled) {
            this.listeners.add(new DebugListener());
        }
        
        // Register settings
        Settings.register();

        // Initialize the listener, add filters as necessary
        ChatModeratorListener moderatorListener = new ChatModeratorListener(this);
        setUpFilters(moderatorListener);
        setUpZones(moderatorListener);

        this.listeners.add(moderatorListener);

        // And register all the events.
        for (Listener listener : this.listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("chatmoderator.reload")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        this.onDisable();
        this.onEnable();

        sender.sendMessage(ChatColor.AQUA + "[ChatModerator] - " + ChatColor.DARK_GREEN + "Successfully reloaded config and registered all filters and zones.");

        return true;
    }

    /**
     * Sets up all the filters in the CMListener.
     *
     * Lower priorities gets run first.
     */
    private void setUpFilters(ChatModeratorListener moderatorListener) {
        moderatorListener.getFilterManager().registerFilter(
            new DuplicateMessageFilter(
                this.getPlayerManager(),
                new Permission("chatmoderator.filters.duplicatemessage.exempt"),
                getConfig().getLong("config.delay-between-messages"),
                getConfig().getInt("filters.duplicate-messages.priority")
            ));

        moderatorListener.getFilterManager().registerFilter(
            new IPFilter(
                this.getPlayerManager(),
                new Permission("chatmoderator.filters.ipfilter.exempt"),
                getConfig().getInt("filters.server-ip.priority"),
                FixStyleApplicant.FixStyle.getFixStyleFor(getConfig().getString("filters.ipfilter.fix-style", "NONE"))
            ));

        moderatorListener.getFilterManager().registerFilter(
            new ProfanityFilter(
                this.getPlayerManager(),
                new Permission("chatmoderator.filters.profanity.exempt"),
                new TemplateFactory(this, "filters.profanity").build().getWeights(),
                getConfig().getInt("filters.profanity.priority"),
                new WhitelistFactory(this, "filters.profanity.whitelist").build().getWhitelist(),
                FixStyleApplicant.FixStyle.getFixStyleFor(getConfig().getString("filters.profanity.fix-style", "NONE"))
            ));

        moderatorListener.getFilterManager().registerFilter(
            new AllCapsFilter(
                this.getPlayerManager(),
                new Permission("chatmoderator.filters.all-caps.exempt"),
                getConfig().getInt("filters.all-caps.priority"),
                new WhitelistFactory(this, "filters.all-caps.whitelist").build().getWhitelist(),
                (short) getConfig().getInt("filters.all-caps.max-length", -1)
            ));

        moderatorListener.getFilterManager().registerFilter(
            new RepeatedCharactersFilter(
                this.getPlayerManager(),
                new Permission("chatmoderator.filters.repeatedcharacter.exempt"),
                getConfig().getInt("filters.repeated-characters.count"),
                getConfig().getInt("filters.repeated-characters.priority")
            ));

        moderatorListener.getFilterManager().registerFilter(
            new LeetSpeakFilter(
                this.getPlayerManager(),
                new Permission("chatmoderator.filters.leet.exempt"),
                getConfig().getInt("filters.leet.priority"),
                new LeetSpeakFilterFactory(this, new File(this.getDataFolder(), "dictionary.yml"), "dictionary").build().getDictionary()
            ));
    }

    /**
     * Sets up all the zones for the ChatModeratorListener.
     *
     * @param moderatorListener The {@link tc.oc.chatmoderator.listeners.ChatModeratorListener} to work off of.
     */
    private void setUpZones(ChatModeratorListener moderatorListener) {
        moderatorListener.getZoneManager().registerZone(ZoneType.CHAT, new ZoneFactory(this, "zones.chat").build().getZone());
        moderatorListener.getZoneManager().registerZone(ZoneType.SIGN, new ZoneFactory(this, "zones.signs").build().getZone());
    }

    /**
     * Gets the player manager.
     *
     * @return The player manager.
     */
    public final PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
