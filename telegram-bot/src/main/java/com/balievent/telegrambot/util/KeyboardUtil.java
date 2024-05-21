package com.balievent.telegrambot.util;

import com.balievent.telegrambot.constant.TelegramButton;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KeyboardUtil {

    private static final int EVENT_LOCATIONS_SELECTION_COLS_COUNT = 2;

    public InlineKeyboardMarkup createInlineKeyboard(final TelegramButton telegramButton) {
        final InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder()
            .text(telegramButton.getButtonText())
            .callbackData(telegramButton.getCallbackData())
            .build();

        final List<InlineKeyboardButton> rowInline = List.of(inlineKeyboardButton);
        final List<List<InlineKeyboardButton>> rowsInline = List.of(rowInline);

        return InlineKeyboardMarkup.builder()
            .keyboard(rowsInline)
            .build();
    }

    public InlineKeyboardMarkup createMonthInlineKeyboard(final LocalDate calendarDate) {
        final int monthValue = calendarDate.getMonthValue();

        final InlineKeyboardButton previousMonthButton = InlineKeyboardButton.builder()
            .text(getPreviousMonthButtonText(monthValue))
            .callbackData(TelegramButton.PREVIOUS_MONTH_PAGE.getCallbackData())
            .build();

        final InlineKeyboardButton gotoFilterButton = InlineKeyboardButton.builder()
            .text(TelegramButton.EVENT_START_FILTER.getButtonText())
            .callbackData(TelegramButton.EVENT_START_FILTER.getCallbackData())
            .build();

        final InlineKeyboardButton nextMonthButton = InlineKeyboardButton.builder()
            .text(getNextMonthButtonText(monthValue))
            .callbackData(TelegramButton.NEXT_MONTH_PAGE.getCallbackData())
            .build();

        return InlineKeyboardMarkup.builder()
            .keyboard(List.of(
                List.of(previousMonthButton, gotoFilterButton, nextMonthButton)
            ))
            .build();
    }

    private static String getNextMonthButtonText(final int currentMonth) {
        final Month monthNext = Month.of(currentMonth % 12 + 1);
        return String.format(TelegramButton.NEXT_MONTH_PAGE.getButtonText().formatted(monthNext));
    }

    private static String getPreviousMonthButtonText(final int currentMonth) {
        final Month monthPrevious = Month.of((currentMonth + 10) % 12 + 1);
        return String.format(TelegramButton.PREVIOUS_MONTH_PAGE.getButtonText().formatted(monthPrevious));
    }

    /**
     * Generates an inline keyboard markup for navigating between pages of day events.
     * Can include buttons for the first, previous, next, and last pages (if pageCount > 0)
     * Also includes a button to return to the month view
     *
     * @param currentPage The current page number.
     * @param pageCount   The total number of pages.
     * @return The generated InlineKeyboardMarkup.
     */
    public static InlineKeyboardMarkup getDayEventsKeyboard(final int currentPage, final int pageCount) {
        final List<InlineKeyboardButton> monthBackButtons = new ArrayList<>();
        monthBackButtons.add(InlineKeyboardButton.builder()
            .text(TelegramButton.MONTH_EVENTS_PAGE.getButtonText())
            .callbackData(TelegramButton.MONTH_EVENTS_PAGE.getCallbackData())
            .build());

        if (pageCount == 0) {
            return InlineKeyboardMarkup.builder()
                .keyboard(List.of(monthBackButtons))
                .build();
        }

        final List<InlineKeyboardButton> paginationButtons = new ArrayList<>();
        if (currentPage > 2) {
            final TelegramButton firstEventsPageButton = TelegramButton.FIRST_EVENTS_PAGE;
            paginationButtons.add(InlineKeyboardButton.builder()
                .text(firstEventsPageButton.getButtonText().formatted(pageCount))
                .callbackData(firstEventsPageButton.getCallbackData())
                .build());
        }

        if (currentPage > 1) {
            final TelegramButton previousEventsPageButton = TelegramButton.PREVIOUS_EVENTS_PAGE;
            paginationButtons.add(InlineKeyboardButton.builder()
                .text(previousEventsPageButton.getButtonText().formatted(currentPage - 1, pageCount))
                .callbackData(previousEventsPageButton.getCallbackData())
                .build());
        }

        if (currentPage < pageCount) {
            final TelegramButton nextEventsPageButton = TelegramButton.NEXT_EVENTS_PAGE;
            paginationButtons.add(InlineKeyboardButton.builder()
                .text(nextEventsPageButton.getButtonText().formatted(currentPage + 1, pageCount))
                .callbackData(nextEventsPageButton.getCallbackData())
                .build());
        }

        if (currentPage < pageCount - 1) {
            final TelegramButton lastEventsPageButton = TelegramButton.LAST_EVENTS_PAGE;
            paginationButtons.add(InlineKeyboardButton.builder()
                .text(lastEventsPageButton.getButtonText().formatted(pageCount, pageCount))
                .callbackData(lastEventsPageButton.getCallbackData())
                .build());
        }

        return InlineKeyboardMarkup.builder()
            .keyboard(List.of(paginationButtons, monthBackButtons))
            .build();
    }

    public static InlineKeyboardMarkup getDetailedLocationKeyboard() {
        final List<InlineKeyboardButton> monthBackButtons = new ArrayList<>();
        monthBackButtons.add(InlineKeyboardButton.builder()
            .text(TelegramButton.DAY_EVENT_PAGE.getButtonText())
            .callbackData(TelegramButton.DAY_EVENT_PAGE.getCallbackData())
            .build());

        return InlineKeyboardMarkup.builder()
            .keyboard(List.of(monthBackButtons))
            .build();
    }

    public static InlineKeyboardMarkup getShowMoreOptionsKeyboard(final int countEvent) {
        final List<InlineKeyboardButton> monthBackButtons = new ArrayList<>();
        monthBackButtons.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SHOW_MORE_EVENTS.getButtonText().formatted(countEvent))
            .callbackData(TelegramButton.SHOW_MORE_EVENTS.getCallbackData())
            .build());

        return InlineKeyboardMarkup.builder()
            .keyboard(List.of(monthBackButtons))
            .build();
    }

    //    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
    //        final List<KeyboardRow> keyboard = new ArrayList<>();
    //
    //        final KeyboardRow row = new KeyboardRow();
    //        row.add(TelegramButton.CHANGE_SEARCH_PARAMETERS.getButtonText());
    //        keyboard.add(row);
    //
    //        final KeyboardRow settingsAndHelp = new KeyboardRow();
    //        settingsAndHelp.add(TelegramButton.SETTINGS_AND_HELP.getButtonText());
    //        keyboard.add(settingsAndHelp);
    //
    //        final KeyboardRow contactAndFavorite = new KeyboardRow();
    //        contactAndFavorite.add(TelegramButton.CONTACT_US.getButtonText());
    //        keyboard.add(contactAndFavorite);
    //
    //        return ReplyKeyboardMarkup.builder()
    //            .keyboard(keyboard)
    //            .build();
    //    }

    public static InlineKeyboardMarkup createEventDateSelectionKeyboard() {

        final List<InlineKeyboardButton> firstRow = new ArrayList<>();

        firstRow.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SEARCH_TODAY_EVENTS.getButtonText())
            .callbackData(TelegramButton.SEARCH_TODAY_EVENTS.getCallbackData())
            .build());

        firstRow.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SEARCH_TOMORROW_EVENTS.getButtonText())
            .callbackData(TelegramButton.SEARCH_TOMORROW_EVENTS.getCallbackData())
            .build());

        final List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SEARCH_THIS_WEEK_EVENTS.getButtonText())
            .callbackData(TelegramButton.SEARCH_THIS_WEEK_EVENTS.getCallbackData())
            .build());
        secondRow.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SEARCH_NEXT_WEEK_EVENTS.getButtonText())
            .callbackData(TelegramButton.SEARCH_NEXT_WEEK_EVENTS.getCallbackData())
            .build());

        final List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        thirdRow.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SEARCH_ON_THIS_WEEKEND_EVENTS.getButtonText())
            .callbackData(TelegramButton.SEARCH_ON_THIS_WEEKEND_EVENTS.getCallbackData())
            .build());

        thirdRow.add(InlineKeyboardButton.builder()
            .text(TelegramButton.SEARCH_SHOW_ALL_EVENTS.getButtonText())
            .callbackData(TelegramButton.SEARCH_SHOW_ALL_EVENTS.getCallbackData())
            .build());

        return InlineKeyboardMarkup.builder()
            .keyboard(List.of(firstRow, secondRow, thirdRow))
            .build();
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public static InlineKeyboardMarkup createEventLocationsSelectionKeyboard(final List<String> allLocations,
                                                                             final List<String> selectedLocations) {
        final List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (final String locationId : allLocations) {
            final String selectedIcon = selectedLocations.contains(locationId)
                                        ? "✅ "
                                        : "👉 ";

            row.add(InlineKeyboardButton.builder()
                .text(selectedIcon + locationId)
                .callbackData(locationId)
                .build());

            if (row.size() == EVENT_LOCATIONS_SELECTION_COLS_COUNT) {
                keyboard.add(row);
                row = new ArrayList<>();
            }
        }

        if (!row.isEmpty()) {
            keyboard.add(row);
        }

        // Добавляем кнопки "DESELECT_ALL"
        if (selectedLocations.contains(TelegramButton.DESELECT_ALL_LOCATIONS.getCallbackData())) {
            addNewButton(keyboard, TelegramButton.DESELECT_ALL_LOCATIONS.getButtonText(), TelegramButton.DESELECT_ALL_LOCATIONS.getCallbackData());
            // Добавляем кнопки "SELECT_ALL"
        } else if (selectedLocations.contains(TelegramButton.SELECT_ALL_LOCATIONS.getCallbackData())) {
            addNewButton(keyboard, TelegramButton.SELECT_ALL_LOCATIONS.getButtonText(), TelegramButton.SELECT_ALL_LOCATIONS.getCallbackData());
        }

        // Добавляем кнопки "Next"
        addNewButton(keyboard, TelegramButton.EVENT_LOCATIONS_NEXT.getButtonText(), TelegramButton.EVENT_LOCATIONS_NEXT.getCallbackData());

        return InlineKeyboardMarkup.builder()
            .keyboard(keyboard)
            .build();
    }

    private static void addNewButton(final List<List<InlineKeyboardButton>> keyboard,
                                     final String testString,
                                     final String callbackData) {
        final List<InlineKeyboardButton> nextButton = new ArrayList<>();
        nextButton.add(InlineKeyboardButton.builder()
            .text(testString)
            .callbackData(callbackData)
            .build());

        keyboard.add(nextButton);
    }

}

