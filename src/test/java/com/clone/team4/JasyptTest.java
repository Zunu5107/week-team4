package com.clone.team4;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptTest {

//    @Autowired
//    public void init(@Value("${custom.option.testPassword}") String decryptedText){
//        System.out.println("decryptedValueText = " + decryptedText);
//    }

    @Test
    public void EncodeTest(){
        String password = "*wellbring112";

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("7JWU7Zi47ZmU7ZWY64KY7ZWg66Ck6rOg7J2066CH6rKM7Z6Y65Ok6rKM6rmM7KeA7ZW07JW87ZWY64KY");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);


        String encryptedText = encryptor.encrypt(password);
        String decryptedText = encryptor.decrypt(encryptedText);

        System.out.println("encryptedText = " + encryptedText);
        System.out.println("decryptedText = " + decryptedText);
    }
}
