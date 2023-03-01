package belykh.projects.TelegramBot.out.rest.api.request.wrapper.archivist;

/**
 * Endpoints RestApi - Archivist
 */
public enum ArchivistApiEndPoints {
    REPORT("/report/{periodReport}");

    private final String commandPath;

    ArchivistApiEndPoints(String commandPath) {
        this.commandPath = commandPath;
    }

    public String getCommandPath() {
        return commandPath;
    }
}
