package com.cortexia.cortexia_back_end.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmail(String para, String assunto, String mensagem) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(para);
        mail.setSubject(assunto);
        mail.setText(mensagem);

        mailSender.send(mail);
    }
}
