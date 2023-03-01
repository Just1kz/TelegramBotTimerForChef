package belykh.projects.TelegramBot.out.rest.api.request.wrapper.timer;

import belykh.projects.TelegramBot.service.Dishes;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static belykh.projects.TelegramBot.service.BussinesConstants.*;

/**
 * Сервис для работы с RestApi - Timer
 * @author Belykh.A.S
 */
@Log4j
@Component
public class TimerRequestServiceWrapper implements TimerRequestService {

    @Value("${timer.rest.api.url}")
    private String timerApiMainUrl;
    private RestOperations restOperations;

    private static final String RESPONSE_THEN_ERROR_REQUEST = "При вызове сервиса Timer возникла ошибка = %s";

    @PostConstruct
    private void init() {
        restOperations = new RestTemplate();
    }

    @Override
    public String resetAllTimers() {
        String responseText;

        try {
            log.debug(timerApiMainUrl + TimerApiEndPoints.RESET_ALL.getCommandPath());
            restOperations.delete(timerApiMainUrl + TimerApiEndPoints.RESET_ALL.getCommandPath());
            responseText = EmojiParser.parseToUnicode("Все таймеры сброшены! Можете запускать занаво!" + MAN_COOK);
        } catch (RestClientException ex) {
            ex.printStackTrace();
            responseText = ex.getMessage();
        }

        log.debug("TimerRequestService.resetAllTimers, responseText = " + responseText);

        return responseText;
    }

    @Override
    public String startTimer(String keyObject) {
        String responseText;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        try {
            HttpEntity<String> requestBody = new HttpEntity<>(keyObject);
            log.debug(timerApiMainUrl + TimerApiEndPoints.START.getCommandPath());
            responseEntity = restOperations.postForEntity(timerApiMainUrl + TimerApiEndPoints.START.getCommandPath(), requestBody, String.class);
            responseText = EmojiParser.parseToUnicode("Таймер для " + Dishes.getAttributesByMapper(keyObject, Dishes::getSecondRusName) + " запущен! " + HOURGLASS_FLOWING_SAND);
        } catch (RestClientException ex) {
            ex.printStackTrace();
            responseText = ex.getMessage();
        }

        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return String.format(RESPONSE_THEN_ERROR_REQUEST, responseText);
        }

        log.debug("TimerRequestService.startTimer, responseText = " + responseText);

        return responseText;
    }

    @Override
    public String stopTimer(String keyObject) {
        String responseText;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        try {
            HttpEntity<String> requestBody = new HttpEntity<>(keyObject);
            log.debug(timerApiMainUrl + TimerApiEndPoints.START.getCommandPath());
            responseEntity = restOperations.postForEntity(timerApiMainUrl + TimerApiEndPoints.STOP.getCommandPath(), requestBody, String.class);
            responseText = EmojiParser.parseToUnicode("Таймер для " + Dishes.getAttributesByMapper(keyObject, Dishes::getSecondRusName) + " отключен!" + HOURGLASS + "время приготовления составило: " + responseEntity.getBody());
        } catch (RestClientException ex) {
            ex.printStackTrace();
            responseText = ex.getMessage();
        }

        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return String.format(RESPONSE_THEN_ERROR_REQUEST, responseText);
        }

        log.debug("TimerRequestService.stopTimer, responseText = " + responseText);

        return responseText;
    }

    @Override
    public Map<String, String> getAllActiveTimers() {
        Map<String, String> data = new HashMap<>();
        try {
            log.debug(timerApiMainUrl + TimerApiEndPoints.GET_ACTIVE_TIMERS.getCommandPath());
            data = restOperations.getForObject(timerApiMainUrl + TimerApiEndPoints.GET_ACTIVE_TIMERS.getCommandPath(), HashMap.class);
        } catch (RestClientException ex) {
            ex.printStackTrace();
        }

        log.debug("TimerRequestService.getAllActiveTimers, responseText = " + data);

        return data;
    }
}
