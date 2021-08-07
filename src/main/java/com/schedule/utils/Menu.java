package com.schedule.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Menu {

    StudentsGroups(new String[][]{
            new String[]{"\uD83C\uDF93Перелік всіх груп\uD83C\uDF93"}
    }),

    Main(new String[][]{
            new String[]{"\uD83D\uDCDAРозклад на сьогодні\uD83D\uDCDA"},
            new String[]{"\uD83D\uDDD3Розклад на інший день\uD83D\uDDD3"},
            new String[]{"\uD83D\uDCD6Додаткова інформація\uD83D\uDCD6"},
            new String[]{"\uD83C\uDF93Змінити групу\uD83C\uDF93"}
    }),

    Days(new String[][]{
            new String[]{"Понеділок"},
            new String[]{"Вівторок"},
            new String[]{"Середа"},
            new String[]{"Четвер"},
            new String[]{"П'ятниця"},
            new String[]{"Субота"},
            new String[]{"Неділя"},
            new String[]{"Головне меню\uD83D\uDD3C"}
    }),

    AdditionalInfo(new String[][]{
            new String[]{"⏰Розклад початку та завершення занять⏰"},
            new String[]{"✉Контактна інформація викладачів✉"},
            new String[]{"Головне меню\uD83D\uDD3C"}
    }),

    AboutTeachers(new String[][]{
            new String[]{"\uD83D\uDCD4Всі викладачі\uD83D\uDCD4"},
            new String[]{"\uD83D\uDD0DПошук викладача за ім'ям\uD83D\uDD0D"},
            new String[]{"Головне меню\uD83D\uDD3C"}
    });

    private final String[][] menu;
}
