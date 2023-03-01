package belykh.projects.TelegramBot.out.rest.api.request.wrapper.timer;

import java.util.Map;

/**
 * Сервис для работы с RestApi - Timer
 * @author Belykh.A.S
 */
public interface TimerRequestService {

    String resetAllTimers();

    String startTimer(String keyObject);

    String stopTimer(String keyObject);

    Map<String, String> getAllActiveTimers();
}
