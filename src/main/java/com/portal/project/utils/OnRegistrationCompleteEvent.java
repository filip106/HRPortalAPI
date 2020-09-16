package com.portal.project.utils;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.portal.project.model.ApplicationUser;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String appUrl;
    private Locale locale;
    private ApplicationUser user;
 
    public OnRegistrationCompleteEvent(ApplicationUser user, Locale locale, String appUrl) {
        super(user);
        
        this.user = user;
        this.locale = locale;	
        this.appUrl = appUrl;
    }
}
