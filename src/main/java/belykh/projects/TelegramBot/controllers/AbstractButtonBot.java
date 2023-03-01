package belykh.projects.TelegramBot.controllers;

import belykh.projects.TelegramBot.out.rest.api.request.wrapper.archivist.ArchivistRequestService;
import belykh.projects.TelegramBot.out.rest.api.request.wrapper.timer.TimerRequestService;
import belykh.projects.TelegramBot.service.Dishes;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static belykh.projects.TelegramBot.service.BussinesConstants.*;
import static belykh.projects.TelegramBot.service.BussinesConstants.TIMER_STAKE_BUTTON;

@Log4j
public abstract class AbstractButtonBot extends AbstractCommandBot{

    protected EditMessageText responseWithEditMessage;

    protected AbstractButtonBot(ArchivistRequestService archivistRequestService,
                                TimerRequestService timerRequestService) {
        super(archivistRequestService, timerRequestService);
    }

    protected void processButton(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        responseWithEditMessage.setChatId(chatId);
        responseWithEditMessage.setMessageId(messageId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        String responseText;


        switch (callbackData) {
            case ADD_DUMPLINGS_BUTTON:
                InlineKeyboardButton dumplingsButton = new InlineKeyboardButton();
                dumplingsButton.setText(EmojiParser.parseToUnicode(Dishes.DUMPLINGS.getFirstRusName() + Dishes.DUMPLINGS.getEmojiKey()));
                dumplingsButton.setCallbackData(TIMER_DUMPLINGS_BUTTON);
                mainTimeMenu.putIfAbsent(TIMER_DUMPLINGS_BUTTON, dumplingsButton);
                responseText = EmojiParser.parseToUnicode("Добавлено блюдо -" + Dishes.DUMPLINGS.getFirstRusName() + Dishes.DUMPLINGS.getEmojiKey());
                responseWithEditMessage.setText(responseText);
                break;
            case ADD_CAKE_BUTTON:
                InlineKeyboardButton cakeButton = new InlineKeyboardButton();
                cakeButton.setText(EmojiParser.parseToUnicode(Dishes.CAKE.getFirstRusName() + Dishes.CAKE.getEmojiKey()));
                cakeButton.setCallbackData(TIMER_CAKE_BUTTON);
                mainTimeMenu.putIfAbsent(TIMER_CAKE_BUTTON, cakeButton);
                responseText = EmojiParser.parseToUnicode("Добавлено блюдо -" + Dishes.CAKE.getFirstRusName() + Dishes.CAKE.getEmojiKey());
                responseWithEditMessage.setText(responseText);
                break;
            case ADD_STAKE_BUTTON:
                InlineKeyboardButton stakeButton = new InlineKeyboardButton();
                stakeButton.setText(EmojiParser.parseToUnicode(Dishes.STAKE.getFirstRusName() + Dishes.STAKE.getEmojiKey()));
                stakeButton.setCallbackData(TIMER_STAKE_BUTTON);
                mainTimeMenu.putIfAbsent(TIMER_STAKE_BUTTON, stakeButton);
                responseText = EmojiParser.parseToUnicode("Добавлено блюдо -" + Dishes.STAKE.getFirstRusName() + Dishes.STAKE.getEmojiKey());
                responseWithEditMessage.setText(responseText);
                break;

            case TIMER_DUMPLINGS_BUTTON:
                InlineKeyboardButton editDumplingsButton = new InlineKeyboardButton();
                editDumplingsButton.setCallbackData(TIMER_DUMPLINGS_BUTTON);

                if (timerDumplingsIsActive) {
                    timerDumplingsIsActive = false;
                    responseText = timerRequestService.stopTimer(Dishes.DUMPLINGS.name());
                    editDumplingsButton.setText(EmojiParser.parseToUnicode(Dishes.DUMPLINGS.getFirstRusName() + Dishes.DUMPLINGS.getEmojiKey() + WHITE_CHECK_MARK));
                } else {
                    timerDumplingsIsActive = true;
                    responseText = timerRequestService.startTimer(Dishes.DUMPLINGS.name());
                    editDumplingsButton.setText(EmojiParser.parseToUnicode(Dishes.DUMPLINGS.getFirstRusName() + Dishes.DUMPLINGS.getEmojiKey() + HOURGLASS_FLOWING_SAND));
                }
                mainTimeMenu.put(TIMER_DUMPLINGS_BUTTON, editDumplingsButton);
                responseWithEditMessage.setText(responseText);
                break;

            case TIMER_CAKE_BUTTON:
                InlineKeyboardButton editCakeButton = new InlineKeyboardButton();
                editCakeButton.setCallbackData(TIMER_CAKE_BUTTON);

                if (timerCakeIsActive) {
                    timerCakeIsActive = false;
                    responseText = timerRequestService.stopTimer(Dishes.CAKE.name());
                    editCakeButton.setText(EmojiParser.parseToUnicode(Dishes.CAKE.getFirstRusName() + Dishes.CAKE.getEmojiKey() + WHITE_CHECK_MARK));
                } else {
                    timerCakeIsActive = true;
                    responseText = timerRequestService.startTimer(Dishes.CAKE.name());
                    editCakeButton.setText(EmojiParser.parseToUnicode(Dishes.CAKE.getFirstRusName() + Dishes.CAKE.getEmojiKey() + HOURGLASS_FLOWING_SAND));
                }

                mainTimeMenu.put(TIMER_CAKE_BUTTON, editCakeButton);
                responseWithEditMessage.setText(responseText);
                break;

            case TIMER_STAKE_BUTTON:
                InlineKeyboardButton editStakeButton = new InlineKeyboardButton();
                editStakeButton.setCallbackData(TIMER_STAKE_BUTTON);

                if (timerStakeIsActive) {
                    timerStakeIsActive = false;
                    responseText = timerRequestService.stopTimer(Dishes.STAKE.name());
                    editStakeButton.setText(EmojiParser.parseToUnicode(Dishes.STAKE.getFirstRusName() + Dishes.STAKE.getEmojiKey() + WHITE_CHECK_MARK));
                } else {
                    timerStakeIsActive = true;
                    responseText = timerRequestService.startTimer(Dishes.STAKE.name());
                    editStakeButton.setText(EmojiParser.parseToUnicode(Dishes.STAKE.getFirstRusName() + Dishes.STAKE.getEmojiKey() + HOURGLASS_FLOWING_SAND));
                }

                mainTimeMenu.put(TIMER_STAKE_BUTTON, editStakeButton);
                responseWithEditMessage.setText(responseText);
                break;

            default:
                responseText = EmojiParser.parseToUnicode("Вы запустили не поддерживаемую кнопку!" + TRIUMPH + " Повторите снова" + PRAY);
                responseWithEditMessage.setText(responseText);
        }

        if (!mainTimeMenu.isEmpty()) {
            rowsInline.add(new ArrayList<>(mainTimeMenu.values()));
            inlineKeyboardMarkup.setKeyboard(rowsInline);
            responseWithEditMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        sendAnswerMessage(responseWithEditMessage);
    }

    protected void sendAnswerMessage(EditMessageText message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }
}
