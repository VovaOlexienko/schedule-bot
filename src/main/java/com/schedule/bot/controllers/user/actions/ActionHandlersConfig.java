package com.schedule.bot.controllers.user.actions;

import com.schedule.bot.controllers.user.actions.handlers.ActionHandler;
import com.schedule.bot.controllers.user.actions.handlers.LastActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
public class ActionHandlersConfig {

    @Autowired
    List<ActionHandler> actionHandlers;

    @EventListener(ApplicationReadyEvent.class)
    public void setActionHandlers() {
        for (int i = 0; i < actionHandlers.size() - 1; i++) {
            actionHandlers.get(i).setNext(actionHandlers.get(i + 1));
        }
        actionHandlers.get(actionHandlers.size() - 1).setNext(new LastActionHandler());
    }
}
