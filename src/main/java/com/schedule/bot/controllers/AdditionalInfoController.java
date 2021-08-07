package com.schedule.bot.controllers;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.schedule.bot.controllers.user.actions.Action;
import com.schedule.bot.security.RolesAllowed;
import com.schedule.dao.StudentDao;
import com.schedule.dao.TeacherDao;
import com.schedule.modal.Role;
import com.schedule.modal.Student;
import com.schedule.utils.Menu;
import com.schedule.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@BotController
public class AdditionalInfoController implements TelegramMvcController {

    @Autowired
    StudentDao studentDao;
    @Autowired
    TeacherDao teacherDao;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "\uD83D\uDCD6Додаткова інформація\uD83D\uDCD6")
    public BaseRequest additionalInfoMenu(Chat chat) {
        return new SendMessage(chat.id(), "Додаткова інформація").replyMarkup(new ReplyKeyboardMarkup(Menu.AdditionalInfo.getMenu()).resizeKeyboard(true).selective(true));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "⏰Розклад початку та завершення занять⏰")
    public String lessonSchedule(Chat chat) {
        return "1 пара  8:50 – 10:10\n" +
                "\n" +
                "2 пара  10:20 – 11:40\n" +
                "\n" +
                "3 пара  12:40 – 14:00\n" +
                "\n" +
                "4 пара  14:10 – 15:30\n" +
                "\n" +
                "5 пара 15:40 – 17:00\n" +
                "\n" +
                "6 пара 17:10 – 18:30";
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "✉Контактна інформація викладачів✉")
    public BaseRequest aboutTeacher(Chat chat) {
        return new SendMessage(chat.id(), "✉Контактна інформація викладачів✉").replyMarkup(new ReplyKeyboardMarkup(Menu.AboutTeachers.getMenu()).resizeKeyboard(true).selective(true));
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "\uD83D\uDCD4Всі викладачі\uD83D\uDCD4")
    public String teachers(Chat chat) {
        return Utils.toStringTeacherList(teacherDao.getAll());
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "\uD83D\uDD0DПошук викладача за ім'ям\uD83D\uDD0D")
    public String findTeachers(Chat chat) {
        Student student = studentDao.getStudentByChatId(chat.id()).get();
        student.setNextAction(Action.FindTeacherByName);
        studentDao.save(student);
        return "Введіть коректне повне ім'я викладача, або частину імені (без граматичних помилок)!";
    }

    @RolesAllowed(roles = {Role.Admin, Role.User})
    @MessageRequest(value = "Головне меню\uD83D\uDD3C")
    public BaseRequest backToMainMenu(Chat chat) {
        return new SendMessage(chat.id(), "Головне меню").replyMarkup(new ReplyKeyboardMarkup(Menu.Main.getMenu()).resizeKeyboard(true).selective(true));
    }
}
