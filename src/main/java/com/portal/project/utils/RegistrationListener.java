package com.portal.project.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.portal.project.constants.FrontEndConstants;
import com.portal.project.model.ApplicationUser;
import com.portal.project.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{

	@Autowired
    private UserService userService;
	
//	@Autowired
//    private MessageSource messages;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
        ApplicationUser user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/confirm-registration?token=" + token;
//        String message = messages.getMessage("message.regSucc", null, event.getLocale());
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("You need to folow url in order to register" + "\r\n" + FrontEndConstants.FRONTEND_URL + confirmationUrl);
        mailSender.send(email);
    }
}
