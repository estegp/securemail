/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.estegp.secure.mail.mimemultipart;

/**
 * This class if a factory that will get the class that implements the 
 * best email encryption base on the given certificates.
 */
public class EncryptMailFactory {
    /** The identifier of the SMIME encryption */
    public static final int SMIME = 0;
    /** The identifier of the PGP encryption */
    public static final int PGP = 1;
    /** The type of encryption choose during the construction of the class */
    private final int typeEncryption;
    /** The class that will be used for encryption */
    private final EncryptMail encryptMail;
    
    /**
     * Constructor
     * Sets the email encryption class base on the give type
     * @param typeEncryption the type of encryption
     * @param key the public certificate used to encrypt the emails
     */
    public EncryptMailFactory(int typeEncryption, byte[] key){
        this.typeEncryption = typeEncryption;
        if(this.typeEncryption == SMIME){ // SMIME
            this.encryptMail = new EncryptSMIME(key);
        }
        else{ // PGP
            this.encryptMail = new EncryptMailPGP(key);
        }
    }
    
     /**
     * Constructor
     * Sets the email encryption class base on the give certificate
     * @param key the public certificate used to encrypt the emails
     */
    public EncryptMailFactory(byte[] key){
        this.typeEncryption = this.getTypeEncryption(key);
        if(this.typeEncryption == SMIME){ // SMIME
            this.encryptMail = new EncryptSMIME(key);
        }
        else{   // PGP
            this.encryptMail = new EncryptMailPGP(key);
        }
    }
    
    /**
     * Gets the type of encryption need to encrypt an email with the given
     * public key.
     * @param key the certificate/public key
     * @return the identifier of the type of encryption
     */
    private int getTypeEncryption(byte[] key){
        String keyS = new String(key);
        
        if(keyS.contains("BEGIN PGP PUBLIC KEY BLOCK")){
            return PGP;
        }
        else{
            return SMIME;
        }
    }
    
    /**
     * Get an instace of the class that will be used to encrypt the email.
     * @return the instance of the class.
     */
    public EncryptMail getEncryptor(){
        return this.encryptMail;
    }
    
}
