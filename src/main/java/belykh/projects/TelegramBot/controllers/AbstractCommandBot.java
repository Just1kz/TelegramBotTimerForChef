package belykh.projects.TelegramBot.controllers;

import belykh.projects.TelegramBot.out.rest.api.request.wrapper.archivist.ArchivistRequestService;
import belykh.projects.TelegramBot.out.rest.api.request.wrapper.timer.TimerRequestService;
import belykh.projects.TelegramBot.service.Dishes;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static belykh.projects.TelegramBot.service.BussinesConstants.*;

@Log4j
public abstract class AbstractCommandBot extends TelegramLongPollingBot {

    protected final ArchivistRequestService archivistRequestService;
    protected final TimerRequestService timerRequestService;
    protected Map<String, InlineKeyboardButton> mainTimeMenu;
    protected boolean timerDumplingsIsActive;
    protected boolean timerCakeIsActive;
    protected boolean timerStakeIsActive;
    protected SendMessage response;

    protected AbstractCommandBot(ArchivistRequestService archivistRequestService, TimerRequestService timerRequestService) {
        this.archivistRequestService = archivistRequestService;
        this.timerRequestService = timerRequestService;
    }

    protected void processCommand(Message message) {
        String userCommand = message.getText();
        response.setChatId(message.getChatId());

        String responseText;

        switch (userCommand) {
            case START:
                initKeyBoardParams();
                responseText = EmojiParser.parseToUnicode("Добро пожаловать в помощник подсчета времени по приготовлению блюд!" + STEW + STOP_WATCH);
                response.setText(responseText);
                sendAnswerMessage(response);
                break;
            case ADD_DISHES:
                if (mainTimeMenu.keySet().size() == 3) {
                    responseText = EmojiParser.parseToUnicode("Все возможные блюда уже добавлены! Просто запустите новый таймер" + MAN_COOK);
                    response.setText(responseText);
                    response.setReplyMarkup(getMarkup());
                } else {
                    responseText = EmojiParser.parseToUnicode("Выберите блюдо!" + MAN_COOK);
                    response.setText(responseText);
                    addMenuForDishes();
                }
                sendAnswerMessage(response);
                break;
            case RESET_TIMERS:
                //сбросить активные таймеры на 0 и вернуть флаги на старт
                //если блюда не добавлены ни одно - нечего сбрасывать

                Map<String, String> data = timerRequestService.getAllActiveTimers();

                if (mainTimeMenu.keySet().size() == 0) {
                    responseText = EmojiParser.parseToUnicode("Ни одно блюдо не добавлено, нечего сбрасывать! Добавьте блюдо и запустите таймер" + MAN_COOK);
                } else if (data != null && data.isEmpty()) {
                    responseText = EmojiParser.parseToUnicode("Отсутствуют запущенные таймеры! Запустите хотя бы один таймер" + MAN_COOK);
                } else {
                    responseText = timerRequestService.resetAllTimers();
                    updateTextButtonAndFlagsAfterResetTimers();
                }

                response.setReplyMarkup(getMarkup());
                response.setText(responseText);
                sendAnswerMessage(response);
                break;
            case GET_REPORT_FOR_WEEK:
                responseText = archivistRequestService.getReport(604800000L);
                response.setReplyMarkup(getMarkup());
                response.setText(responseText);
                sendAnswerMessage(response);
                break;
            default:
                initKeyBoardParams();
                responseText = EmojiParser.parseToUnicode("Это очень интересно, но такая команда не поддерживается! "+ TRIUMPH + " Повторите снова" + PRAY);
                response.setText(responseText);
                sendAnswerMessage(response);
                break;
        }

    }

    protected void initKeyBoardParams() {
            //добавление клавиатуры
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

            List<KeyboardRow> keyboardRows = new ArrayList<>();

            KeyboardRow row = new KeyboardRow();
            row.add(ADD_DISHES);
            row.add(RESET_TIMERS);
            row.add(GET_REPORT_FOR_WEEK);

            keyboardRows.add(row);
            keyboardMarkup.setKeyboard(keyboardRows);
            response.setReplyMarkup(keyboardMarkup);
    }

    protected InlineKeyboardMarkup getMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkups = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlines = new ArrayList<>();
        rowsInlines.add(new ArrayList<>(mainTimeMenu.values()));
        inlineKeyboardMarkups.setKeyboard(rowsInlines);
        return inlineKeyboardMarkups;
    }

    protected void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    private void addMenuForDishes() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton dumplingsButton = new InlineKeyboardButton();
        dumplingsButton.setText(EmojiParser.parseToUnicode(Dishes.DUMPLINGS.getFirstRusName() + Dishes.DUMPLINGS.getEmojiKey()));
        dumplingsButton.setCallbackData(ADD_DUMPLINGS_BUTTON);

        InlineKeyboardButton cakeButton = new InlineKeyboardButton();
        cakeButton.setText(EmojiParser.parseToUnicode(Dishes.CAKE.getFirstRusName()+ Dishes.CAKE.getEmojiKey()));
        cakeButton.setCallbackData(ADD_CAKE_BUTTON);

        InlineKeyboardButton stakeButton = new InlineKeyboardButton();
        stakeButton.setText(EmojiParser.parseToUnicode(Dishes.STAKE.getFirstRusName()+ Dishes.STAKE.getEmojiKey()));
        stakeButton.setCallbackData(ADD_STAKE_BUTTON);

        rowInline.add(dumplingsButton);
        rowInline.add(cakeButton);
        rowInline.add(stakeButton);

        rowsInline.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        response.setReplyMarkup(inlineKeyboardMarkup);
    }

    private void updateTextButtonAndFlagsAfterResetTimers() {
        timerDumplingsIsActive = false;
        timerCakeIsActive = false;
        timerStakeIsActive = false;

        //изменять текст кнопок после сброса таймеров
        mainTimeMenu.forEach((key, value) -> {
            InlineKeyboardButton button;
            switch (key) {
                case TIMER_DUMPLINGS_BUTTON:
                    button = value;
                    button.setText(EmojiParser.parseToUnicode(Dishes.DUMPLINGS.getFirstRusName() + Dishes.DUMPLINGS.getEmojiKey() + WHITE_CHECK_MARK));
                    break;
                case TIMER_CAKE_BUTTON:
                    button = value;
                    button.setText(EmojiParser.parseToUnicode(Dishes.CAKE.getFirstRusName() + Dishes.CAKE.getEmojiKey() + WHITE_CHECK_MARK));
                    break;
                case TIMER_STAKE_BUTTON:
                    button = value;
                    button.setText(EmojiParser.parseToUnicode(Dishes.STAKE.getFirstRusName() + Dishes.STAKE.getEmojiKey() + WHITE_CHECK_MARK));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + key);
            }
            mainTimeMenu.put(key, button);
        });
    }
}
