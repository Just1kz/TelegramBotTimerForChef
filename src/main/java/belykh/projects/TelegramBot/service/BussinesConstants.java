package belykh.projects.TelegramBot.service;

public class BussinesConstants {
    //ключи для CallBackQuery на добавление кнопок
    public static final String ADD_DUMPLINGS_BUTTON = "ADD_DUMPLINGS_BUTTON";
    public static final String ADD_CAKE_BUTTON = "ADD_CAKE_BUTTON";
    public static final String ADD_STAKE_BUTTON = "ADD_STAKE_BUTTON";

    //ключи для CallBackQuery на запуск таймеров
    public static final String TIMER_DUMPLINGS_BUTTON = "TIMER_DUMPLINGS_BUTTON";
    public static final String TIMER_CAKE_BUTTON = "TIMER_CAKE_BUTTON";
    public static final String TIMER_STAKE_BUTTON = "TIMER_STAKE_BUTTON";

    //команды пользователя
    public static final String START = "/start";
    public static final String ADD_DISHES = "Добавить блюдо";
    public static final String RESET_TIMERS = "Обновить таймеры";
    public static final String GET_REPORT_FOR_WEEK = "Получить статистику за неделю";

    //дополнительные emoji
    public static final String MAN_COOK = " :man_cook: ";
    public static final String TRIUMPH = " :triumph: ";
    public static final String PRAY = " :pray: ";
    public static final String STEW = " :stew: ";
    public static final String STOP_WATCH = " :stopwatch: ";
    public static final String WHITE_CHECK_MARK = " :white_check_mark: ";
    public static final String HOURGLASS_FLOWING_SAND = " :hourglass_flowing_sand: ";
    public static final String HOURGLASS = " :hourglass: ";

    public BussinesConstants() {
        throw new IllegalArgumentException("Unsupported operation");
    }
}
