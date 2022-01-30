package com.hust.webchat.service.impl;

import com.hust.webchat.config.ApplicationProperties;
import com.hust.webchat.domain.User;
import com.hust.webchat.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;


@Service
public class MailServiceImpl implements MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";

    private final JavaMailSender mailSender;

    private final ApplicationProperties applicationProperties;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, ApplicationProperties applicationProperties, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.applicationProperties = applicationProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void sendMailActivate(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendEmailFromTemplate(User user, String template, String titleKey){
        if (Objects.isNull(user.getEmail())) {
            log.debug("Email not exist");
            return;
        } else {
            Locale locale = Locale.forLanguageTag("vn");
            Context context = new Context(locale);
            context.setVariable(USER, user);
            context.setVariable(BASE_URL, applicationProperties.getDomain());
            String content = templateEngine.process(template, context);
            String subject = messageSource.getMessage(titleKey, null, locale);
            sendEmail(user.getEmail(), subject, content, false, true);
        }
    }


    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

}
