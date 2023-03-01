package belykh.projects.TelegramBot.out.rest.api.request.wrapper.archivist;

/**
 * Сервис для работы с RestApi - Archivist
 * @author Belykh.A.S
 */
public interface ArchivistRequestService {
    /**
     * Получение отчета за период
     * @param reportPeriod период в милисекундах
     * @return текстовое построчное представление отчета
     */
    String getReport(Long reportPeriod);
}
