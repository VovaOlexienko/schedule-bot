package com.schedule.utils;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class TelegramMenu {

    public static Keyboard getMainMenu() {
        return new ReplyKeyboardMarkup(
                new String[]{"\uD83D\uDCDAРозклад на сьогодні\uD83D\uDCDA"},
                new String[]{"\uD83D\uDDD3Розклад на інший день\uD83D\uDDD3"},
                new String[]{"⏰Розклад початку та завершення занять⏰"},
                new String[]{"\uD83C\uDF93Змінити групу\uD83C\uDF93"})
                .resizeKeyboard(true)
                .selective(true);
    }

    public static Keyboard getDayMenu() {
        return new ReplyKeyboardMarkup(
                new String[]{"Понеділок"},
                new String[]{"Вівторок"},
                new String[]{"Середа"},
                new String[]{"Четвер"},
                new String[]{"П'ятниця"},
                new String[]{"Субота"},
                new String[]{"Неділя"},
                new String[]{"Головне меню\uD83D\uDD3C"})
                .resizeKeyboard(true)
                .selective(true);
    }
}
