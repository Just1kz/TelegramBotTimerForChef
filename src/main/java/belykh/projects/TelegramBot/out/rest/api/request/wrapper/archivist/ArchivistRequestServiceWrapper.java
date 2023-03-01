package belykh.projects.TelegramBot.out.rest.api.request.wrapper.archivist;

import belykh.projects.TelegramBot.dto.ReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static belykh.projects.TelegramBot.service.BussinesConstants.MAN_COOK;

/**
 * Сервис для работы с RestApi - Archivist
 * @author Belykh.A.S
 */
@Log4j
@Component
public class ArchivistRequestServiceWrapper implements ArchivistRequestService {
    @Value("${archivist.rest.api.url}")
    private String archivistApiMainUrl;
    private static ObjectMapper OBJECT_MAPPER;
    private RestOperations restOperations;

    @PostConstruct
    private void init() {
        OBJECT_MAPPER = new ObjectMapper();
        restOperations = new RestTemplate();
    }

    /**
     * Получение отчета за период
     * @param reportPeriod период в милисекундах
     * @return текстовое построчное представление отчета
     */
    @Override
    public String getReport(Long reportPeriod) {
        String rsl;

        List list = new ArrayList<>();
        try {
            log.debug(archivistApiMainUrl + ArchivistApiEndPoints.REPORT.getCommandPath());
            Optional.ofNullable(restOperations.getForObject(archivistApiMainUrl + ArchivistApiEndPoints.REPORT.getCommandPath(), ArrayList.class, 604800000L))
                    .ifPresent(list::addAll);
            log.debug(list);
        } catch (RestClientException ex) {
            ex.printStackTrace();
            rsl = ex.getMessage();
        }

        //проверяем первично полученную структуру из JSON объектов
        if (list.isEmpty()) {
            rsl = EmojiParser.parseToUnicode("Подсчет за последнюю неделю не запускался! Воспользуйтесь сервисом"+ MAN_COOK);
        } else {
            //десериализуем JSON элементы коллекции в классы
            StringJoiner stringJoiner = new StringJoiner("\r\n");

            list.forEach(dataX -> {
                ReportDto dto =OBJECT_MAPPER.convertValue(dataX, ReportDto.class);
                stringJoiner.add(dto.toString());
            });

            rsl = EmojiParser.parseToUnicode("Отчет за неделю" + MAN_COOK + "\r\n" + stringJoiner);
        }

        return rsl;
    }
}
