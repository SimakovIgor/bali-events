package com.balievent.telegrambot.service.handler.common;

import com.balievent.telegrambot.constant.Settings;
import com.balievent.telegrambot.model.entity.UserData;
import com.balievent.telegrambot.service.storage.UserDataService;
import com.balievent.telegrambot.service.support.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaHandler {

    private final EventService eventService;
    private final UserDataService userDataService;

    public SendMediaGroup handleMultipleMedia(final Long chatId, final List<InputMediaPhoto> eventPhotos) {
        return SendMediaGroup.builder()
            .chatId(chatId)
            .medias(new ArrayList<>(eventPhotos))
            .build();
    }

    public SendPhoto handleSingleMedia(final Long chatId, final List<InputMediaPhoto> eventPhotos) {
        return SendPhoto.builder()
            .chatId(chatId)
            .photo(new InputFile(eventPhotos.getFirst().getMedia()))
            .build();
    }

    public List<InputMediaPhoto> findEventPhotos(final Long chatId) {
        final UserData userData = userDataService.getUserData(chatId);
        final int currentPageIndex = userData.getCurrentPage() - 1;
        return eventService.findEvents(userData.getCalendarDate(), currentPageIndex, Settings.PAGE_SIZE)
            .stream()
            .map(event -> {
                final InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                inputMediaPhoto.setMedia(event.getImageUrl());
                return inputMediaPhoto;
            })
            .toList();
    }

}
