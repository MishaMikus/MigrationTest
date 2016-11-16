package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailClient {
    public static void main(String[] args) throws IOException {
        File[] targetList = new File("target").listFiles();
        if (targetList != null && targetList.length != 0) {
            for (File file : targetList) {
                if (file.getName().endsWith(".csv")){
                    new MailClient().sendFile("target/"+file.getName(), "misha.mikus@gmail.com");
                }
            }
        }

    }

    public void sendFile(String fileName, String to_mail) throws IOException {
        Properties props = new Properties();
        String propFilePath = MailClient.class.getClassLoader().getResource("mail.smtp.properties").getPath();
        props.load(new FileInputStream(propFilePath));
        final String user = props.getProperty("mail.smtp.gmail.user");
        final String pass = props.getProperty("mail.smtp.gmail.pass");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user + "@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to_mail));
            String username = System.getProperty("user.name");
            message.setSubject("result from " + username + " " + fileName);
            message.setText("PFA");
            MimeBodyPart messageBodyPart;
            Multipart multipart = new MimeMultipart();
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(fileName)));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
