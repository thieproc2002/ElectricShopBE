
package iuh.edu.service;

import java.io.IOException;

import javax.mail.MessagingException;

import iuh.edu.dto.MailInfo;

public interface SendMailService {

    void run();

    void queue(String to, String subject, String body);

    void queue(MailInfo mail);

    void send(MailInfo mail) throws MessagingException, IOException;

}
