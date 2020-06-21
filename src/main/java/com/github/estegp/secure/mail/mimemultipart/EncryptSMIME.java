/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.estegp.secure.mail.mimemultipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.github.estegp.secure.mail.exceptions.EncryptMailException;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.openpgp.PGPException;

/**
 * This class implements the encryption of emails with SMIME
 */
public class EncryptSMIME extends EncryptMail{
     /**
     * Constructor
     * @param puk the public key used to encrypt the emails.
     */
    public EncryptSMIME(byte[] puk){
        this.puk = puk;
    }
    
    @Override
    public MimeBodyPart encryptMultiPart( MimeMultipart msg, MimeMessage message) throws EncryptMailException {
        try{
            // 1. Sets the msg inside mimebody part
            MimeBodyPart mp = new MimeBodyPart();
            mp.setContent(msg);
            // 2. Encrypts the body part
            return this.encryptData(mp, message);
        }catch (MessagingException ex){
            throw new EncryptMailException(ex);
        }
    }
    
    @Override
    public MimeBodyPart encryptData( MimeBodyPart msg, MimeMessage message) throws EncryptMailException{
        try {
            // The library Directly encrypts the msg and generates a new body part
            SMIMEEnvelopedGenerator gen = new SMIMEEnvelopedGenerator();
            gen.addRecipientInfoGenerator(this.loadEncKey().setProvider("BC"));
            return gen.generate(
                    msg, new JceCMSContentEncryptorBuilder(CMSAlgorithm.RC2_CBC
                    ).setProvider("BC").build());
        }catch (CertificateException | SMIMEException | CMSException | IOException ex){
            throw new EncryptMailException(ex);
        }
    }
    
    /**
     * Loads the public key used to encrypt the emails and initializes the encryptor
     * @return the encryptor used to encrypt the email.
     */
    private JceKeyTransRecipientInfoGenerator loadEncKey() throws CertificateException, IOException {
        // Add Bouncy castles as a security provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        if (Security.getProvider("BC") == null)
        {
            Security.addProvider(new BouncyCastleProvider());
        }

        // Gets certificate chain from byte array
        InputStream bis = new ByteArrayInputStream(this.puk);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        ArrayList<Certificate> certs = new ArrayList<>();
        
        while(bis.available() > 0){
            certs.add(certFactory.generateCertificate(bis));
        }        

        // Build certificate
        X509Certificate certX = (X509Certificate) certs.get(0);
        return new JceKeyTransRecipientInfoGenerator(certX).setProvider("BC");
    }
    
}
