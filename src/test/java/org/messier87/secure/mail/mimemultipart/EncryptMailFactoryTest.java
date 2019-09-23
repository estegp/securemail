package org.messier87.secure.mail.mimemultipart;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class EncryptMailFactoryTest {

    private byte[] genKey = new byte[0];
    private static byte[] pgpKey = null;
    private static byte[] smimeKey = null;

    @BeforeAll
    public static void SetUp() throws IOException, URISyntaxException {
        URL keyFileURL = EncryptMailFactoryTest.class.getClassLoader().getResource("for_testing_only.pgp");
        File dir = new File(keyFileURL.toURI());
        try (InputStream stream = new FileInputStream(dir)){
            EncryptMailFactoryTest.pgpKey = stream.readAllBytes();
        }

        keyFileURL = EncryptMailFactoryTest.class.getClassLoader().getResource("for_testing_only.smime");
        dir = new File(keyFileURL.toURI());
        try (InputStream stream = new FileInputStream(dir)){
            EncryptMailFactoryTest.smimeKey = stream.readAllBytes();
        }

    }

    @Test
    public void TestGetMime() {
        EncryptMail instance = new EncryptMailFactory(EncryptMailFactory.SMIME, this.genKey).getEncryptor();
        assertThat(instance, instanceOf(EncryptSMIME.class));
    }

    @Test
    public void TestGetPGP() {
        EncryptMail instance = new EncryptMailFactory(EncryptMailFactory.PGP, this.genKey).getEncryptor();
        assertThat(instance, instanceOf(EncryptMailPGP.class));
    }

    @Test
    public void TestGetMimeByKey() {
        EncryptMail instance = new EncryptMailFactory(EncryptMailFactoryTest.smimeKey).getEncryptor();
        assertThat(instance, instanceOf(EncryptSMIME.class));
    }

    @Test
    public void TestGetPGPByKey() {
        EncryptMail instance = new EncryptMailFactory(EncryptMailFactoryTest.pgpKey).getEncryptor();
        assertThat(instance, instanceOf(EncryptMailPGP.class));
    }
}