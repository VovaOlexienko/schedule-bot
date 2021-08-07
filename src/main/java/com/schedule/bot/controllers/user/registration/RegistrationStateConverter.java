package com.schedule.bot.controllers.user.registration;


import com.schedule.bot.controllers.user.registration.states.ApprovedState;
import com.schedule.bot.controllers.user.registration.states.EnterGroupState;
import com.schedule.bot.controllers.user.registration.states.RegistrationState;
import com.schedule.bot.controllers.user.registration.states.StartState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
@Service
public class RegistrationStateConverter
        implements AttributeConverter<RegistrationState, String> {

    private static StartState startState;
    private static EnterGroupState enterGroupState;
    private static ApprovedState approvedState;

    @Autowired
    public void setStartState(StartState startState) { RegistrationStateConverter.startState = startState; }

    @Autowired
    public void setEnterGroupState(EnterGroupState enterGroupState) { RegistrationStateConverter.enterGroupState = enterGroupState; }

    @Autowired
    public void setApprovedState(ApprovedState approvedState) { RegistrationStateConverter.approvedState = approvedState; }

    @Override
    public String convertToDatabaseColumn(RegistrationState registrationState) {
        return registrationState.stateName();
    }

    @Override
    public RegistrationState convertToEntityAttribute(String s) {
        if (enterGroupState.stateName().equals(s)) return enterGroupState;
        if (approvedState.stateName().equals(s)) return approvedState;
        return startState;
    }
}