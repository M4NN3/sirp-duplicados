package utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class EncriptarTexto {
    public static String encryptText(String text)
    {
        AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
        aesEncryptor.setPassword(LabelUtils.MASTER_PASSWORD);
        return aesEncryptor.encrypt(text);
    }
    public static String decrptText(String textEncrypted)
    {
        try {
            AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
            aesEncryptor.setPassword(LabelUtils.MASTER_PASSWORD);
            return aesEncryptor.decrypt(textEncrypted);
        }catch (Exception ex){
            return "";
        }
    }
}
