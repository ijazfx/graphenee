package io.graphenee.vaadin.flow.security;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignOutEvent extends ApplicationEvent {

    private String identifer;

    public UserSignOutEvent(Integer id, String idn) {
        super(id);
        this.identifer = idn;
    }
    
}
