package belykh.projects.TelegramBot.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.function.Function;

public enum Dishes {
    DUMPLINGS(" Пельмени ", " Пельменей ",  " :dumpling: "),
    CAKE(" Пирог ", " Пирога ",  " :cake: "),
    STAKE(" Стейк ", " Стейка ",  " :cut_of_meat: ");

    private final String firstRusName;
    private final String secondRusName;
    private final String emojiKey;

    Dishes(String firstRusName, String secondRusName, String emojiKey) {
        this.firstRusName = firstRusName;
        this.secondRusName = secondRusName;
        this.emojiKey = emojiKey;
    }

    public String getFirstRusName() {
        return firstRusName;
    }

    public String getSecondRusName() {
        return secondRusName;
    }

    public String getEmojiKey() {
        return emojiKey;
    }

    public static String getAttributesByMapper(String dishes, Function<Dishes, String> mapper) {
        return Arrays.stream(values())
                .filter(data -> data.name().equals(dishes))
                .map(mapper)
                .findFirst()
                .orElse(StringUtils.EMPTY);
    }
}
