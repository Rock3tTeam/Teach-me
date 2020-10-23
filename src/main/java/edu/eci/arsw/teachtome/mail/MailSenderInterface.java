package edu.eci.arsw.teachtome.mail;

import edu.eci.arsw.teachtome.model.User;

public interface MailSenderInterface {
    void sendCreatedUserEmail(User user);
}
