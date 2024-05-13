//package org.myungkeun.auth_flow.config.mail;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.myungkeun.auth_flow.exception.BusinessLogicException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//
//public class EmailSenderService implements EmailSender{
//
//    private final JavaMailSender emailSender;
//
//    public void sendEmail(String toEmail,
//                          String title,
//                          String text) {
//        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
//        try {
//            emailSender.send(emailForm);
//        } catch (RuntimeException e) {
//            throw new BusinessLogicException(e.getMessage());
//        }
//    }
//
//    // 발신할 이메일 데이터 세팅
//    private SimpleMailMessage createEmailForm(String toEmail,
//                                              String title,
//                                              String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject(title);
//        message.setText(text);
//
//        return message;
//    }
//}
//
