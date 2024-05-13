package org.myungkeun.auth_flow.config.mail;

import jakarta.mail.MessagingException;

public interface EmailSender {
    void sendEmail(String to, String email) throws MessagingException;
}
