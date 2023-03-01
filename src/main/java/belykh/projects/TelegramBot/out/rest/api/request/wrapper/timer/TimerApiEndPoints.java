package belykh.projects.TelegramBot.out.rest.api.request.wrapper.timer;

/**
 * Endpoints RestApi - Timer
 */
public enum TimerApiEndPoints {
    START("/start"),
    STOP("/stop"),
    RESET_ALL("/resetAll"),
    GET_ACTIVE_TIMERS("/activeTimers");

    private final String commandPath;

    TimerApiEndPoints(String commandPath) {
        this.commandPath = commandPath;
    }

    public String getCommandPath() {
        return commandPath;
    }
}
