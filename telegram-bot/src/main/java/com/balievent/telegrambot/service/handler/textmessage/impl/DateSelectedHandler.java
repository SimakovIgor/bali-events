package com.balievent.telegrambot.service.handler.textmessage.impl;

import com.balievent.telegrambot.constant.Settings;
import com.balievent.telegrambot.constant.TextMessageHandlerType;
import com.balievent.telegrambot.constant.TgBotConstants;
import com.balievent.telegrambot.model.dto.BriefDetailedLocationMessageDto;
import com.balievent.telegrambot.model.entity.Event;
import com.balievent.telegrambot.model.entity.UserData;
import com.balievent.telegrambot.service.handler.common.MediaHandler;
import com.balievent.telegrambot.service.handler.textmessage.TextMessageHandler;
import com.balievent.telegrambot.util.KeyboardUtil;
import com.balievent.telegrambot.util.MessageBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateSelectedHandler extends TextMessageHandler {
    private final MediaHandler mediaHandler;

    @Override
    public TextMessageHandlerType getHandlerType() {
        return TextMessageHandlerType.DATE_SELECTED;
    }

    @Override
    public void handle(final Update update) throws TelegramApiException {
        final Long chatId = update.getMessage().getChatId();
        final UserData userData = userDataService.updateCalendarDate(update, false);
        userDataService.saveUserMessageId(update.getMessage().getMessageId(), chatId);

        clearChat(chatId, userData);

        final LocalDate eventsDateFor = userData.getSearchEventDate();

        final int currentPage = 1; //Всегда начинаем с первой страницы
        final List<Event> eventList = eventService.findEvents(eventsDateFor, currentPage - 1, Settings.PAGE_SIZE);
        final int eventCount = eventService.countEvents(eventsDateFor);
        final int pageCount = (eventCount + Settings.PAGE_SIZE - 1) / Settings.PAGE_SIZE;

        userDataService.updatePageInfo(chatId, pageCount, currentPage);

        final ReplyKeyboard replyKeyboard = KeyboardUtil.getDayEventsKeyboard(currentPage, pageCount);
        final String displayDate = eventsDateFor.format(Settings.PRINT_DATE_TIME_FORMATTER);
        final BriefDetailedLocationMessageDto detailedLocationMessageDto = MessageBuilderUtil.buildBriefEventsMessage(currentPage, eventList);

        userDataService.saveOrUpdateLocationMap(detailedLocationMessageDto.getLocationMap(), chatId);

        final SendMessage sendMessage = SendMessage.builder()
            .chatId(chatId)
            .text(TgBotConstants.EVENT_LIST_TEMPLATE.formatted(displayDate, detailedLocationMessageDto.getMessage()))
            .parseMode(ParseMode.HTML)
            .replyMarkup(replyKeyboard)
            .disableWebPagePreview(true)
            .build();

        final Message message = myTelegramBot.execute(sendMessage);
        userDataService.updateLastBotMessageId(message.getMessageId(), chatId);
        mediaHandler.handle(chatId, userData);
    }

}
