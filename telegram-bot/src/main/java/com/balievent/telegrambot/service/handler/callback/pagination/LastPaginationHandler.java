package com.balievent.telegrambot.service.handler.callback.pagination;

import com.balievent.telegrambot.model.entity.UserData;
import com.balievent.telegrambot.service.handler.callback.CallbackHandlerMessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class LastPaginationHandler extends AbstractPaginationHandler {

    @Override
    public CallbackHandlerMessageType getHandlerType() {
        return CallbackHandlerMessageType.LAST_PAGINATION;
    }

    @Override
    protected UserData updateUserData(final Update update) {
        final Long callbackChatId = update.getCallbackQuery().getMessage().getChatId();
        final int pageCount = userDataService.getUserData(callbackChatId).getPageCount();
        return userDataService.setCurrentPage(callbackChatId, pageCount);
    }

}
