package com.schedule.bot.user;


import com.schedule.bot.user.states.ApprovedState;
import com.schedule.bot.user.states.EnterGroupState;
import com.schedule.bot.user.states.UserState;
import com.schedule.bot.user.states.StartState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
@Service
public class UserStateConverter
        implements AttributeConverter<UserState, String> {

    private static StartState startState;
    private static EnterGroupState enterGroupState;
    private static ApprovedState approvedState;

    @Autowired
    public void setStartState(StartState startState) {
        UserStateConverter.startState = startState;
    }

    @Autowired
    public void setEnterGroupState(EnterGroupState enterGroupState) {
        UserStateConverter.enterGroupState = enterGroupState;
    }

    @Autowired
    public void setApprovedState(ApprovedState approvedState) {
        UserStateConverter.approvedState = approvedState;
    }

    @Override
    public String convertToDatabaseColumn(UserState userState) {
        return userState.stateName();
    }

    @Override
    public UserState convertToEntityAttribute(String s) {
        if (enterGroupState.stateName().equals(s)) return enterGroupState;
        if (approvedState.stateName().equals(s)) return approvedState;
        return startState;
    }
}