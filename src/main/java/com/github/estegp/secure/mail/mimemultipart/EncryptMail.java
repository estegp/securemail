/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.estegp.secure.mail.mimemultipart;

import com.github.estegp.secure.mail.exceptions.EncryptMailException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.openpgp.PGPException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.security.cert.CertificateException;

/**
 * This class generalizes the encryption of email
 */
public abstract class EncryptMail {
    protected byte[] puk;
    
    /**
     * This method encrypts a multipart email
     * @param msg the multipart message with the data to encrypt
     * @param message the message that will contain the data
     * @return a mimebody part with the encrypted data
     */
    public abstract MimeBodyPart encryptMultiPart( MimeMultipart msg, MimeMessage message) throws EncryptMailException;

    /**
     * This method encrypts the mime bodypart of an email
     * @param msg the mime multipart with the data to encrypt
     * @param message the message that will contain the data
     * @return a mimebody part with the encrypted data
     */
    public abstract MimeBodyPart encryptData( MimeBodyPart msg, MimeMessage message) throws EncryptMailException;
}
