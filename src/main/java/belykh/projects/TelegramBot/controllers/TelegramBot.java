package belykh.projects.TelegramBot.controllers;

import belykh.projects.TelegramBot.out.rest.api.request.wrapper.archivist.ArchivistRequestService;
import belykh.projects.TelegramBot.out.rest.api.request.wrapper.timer.TimerRequestService;
import belykh.projects.TelegramBot.service.Dishes;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.*;

import static belykh.projects.TelegramBot.service.BussinesConstants.*;

/**
 * Обрабатывает запросы от пользователей передаваемые боту
 * @author Belykh.A.S.
 * @version 1.0.
 */
@Component
@Scope("prototype")
@Log4j
public class TelegramBot extends AbstractButtonBot {

    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    protected TelegramBot(ArchivistRequestService archivistRequestService, TimerRequestService timerRequestService) {
        super(archivistRequestService, timerRequestService);
    }

    @PostConstruct
    private void init() {
        response = new SendMessage();
        responseWithEditMessage = new EditMessageText();
        mainTimeMenu = new LinkedHashMap<>();
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            processCommand(update.getMessage());

        } else if (update.hasCallbackQuery()) {
            processButton(update.getCallbackQuery());
        }
    }
}
