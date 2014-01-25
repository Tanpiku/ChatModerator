package tc.oc.chatmoderator.settings;

import me.anxuiz.settings.Setting;
import me.anxuiz.settings.SettingBuilder;
import me.anxuiz.settings.SettingRegistry;
import me.anxuiz.settings.bukkit.PlayerSettings;
import me.anxuiz.settings.types.EnumType;

public class Settings {

    public static void register() {
        SettingRegistry register = PlayerSettings.getRegistry();
        register.register(FILTER_SETTING);
    }

    public static final Setting FILTER_SETTING = new SettingBuilder()
        .name("ChatModerator").alias("filters").alias("filter").alias("cm")
        .summary("Detail the level of filters you want run on chat")
        .description("ALL: Any messages containing profanity will not be sent\n" +
                     "OFFENSIVE: Any message containing offensive profanity won't be sent\n" +
                     "NONE: All messages will be sent")
        .type(new EnumType<FilterOptions>("Filter Options", FilterOptions.class))
        .defaultValue(FilterOptions.NONE).get();
}
