package com.github.estegp.secure.mail.mimemultipart;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.github.estegp.secure.mail.mimemultipart.helper.MailBuilder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

public class EncryptSMIMETest {

    private static byte[] mimeKey = null;
    private static EncryptSMIME encryptor = null;
    private static MimeMessage message = null;
    private static MimeMultipart msg = null;
    private static MimeBodyPart bodyPart = null;

    @BeforeAll
    public static void SetUp() throws IOException, URISyntaxException, MessagingException {
        URL keyFileURL = EncryptSMIMETest.class.getClassLoader().getResource("for_testing_only.smime");
        File dir = new File(keyFileURL.toURI());
        try (InputStream stream = new FileInputStream(dir)){
            EncryptSMIMETest.mimeKey = stream.readAllBytes();
        }
        EncryptSMIMETest.encryptor = new EncryptSMIME(EncryptSMIMETest.mimeKey);
        EncryptSMIMETest.message = MailBuilder.setGeneralData();
        EncryptSMIMETest.msg = MailBuilder.buildContent();
        EncryptSMIMETest.bodyPart = MailBuilder.buildPart();
    }


    @Test
    public void encryptMultiPart() {
        MimeMultipart msg = new MimeMultipart();
        MimeBodyPart body = EncryptSMIMETest.encryptor.encryptMultiPart(msg, EncryptSMIMETest.message);
        assertNotNull(body);
    }

    @Test
    public void encryptData() {
        MimeBodyPart msg = new MimeBodyPart();
        MimeBodyPart body = EncryptSMIMETest.encryptor.encryptData(msg, EncryptSMIMETest.message);
        assertNotNull(body);
    }
}