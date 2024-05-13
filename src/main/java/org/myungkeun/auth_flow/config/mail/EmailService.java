package org.myungkeun.auth_flow.config.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService implements EmailSender{
    private final JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendEmail(String to, String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        mimeMessageHelper.setText(email, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject("Confirm Your Email");
        mimeMessageHelper.setFrom("audrms3568@gmail.com");
        javaMailSender.send(mimeMessage);
    }
}
