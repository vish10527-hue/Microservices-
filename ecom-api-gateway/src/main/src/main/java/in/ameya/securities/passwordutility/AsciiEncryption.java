package in.ameya.securities.passwordutility;

import org.springframework.stereotype.Component;

@Component
public class AsciiEncryption extends DefaultEncryption
{

    public AsciiEncryption()
    {
    }

    public String crypt(String s)
    {
        return EncryptDecrypt.encrypt(s);
    }

    public boolean matches(String s, String s1)
    {
        boolean flag = false;
        if(s != null && s1 != null)
            try
            {
                String s2 = decrypt(s);
                if(s2 != null)
                    flag = s2.equals(s1);
            }
            catch(Exception exception)
            {
                System.out.println("Error while authentication");
            }
        return flag;
    }

    public String decrypt(String s)
        throws DecryptionNotSupportedException
    {
        String s1 = null;
        try
        {
            s1 = EncryptDecrypt.decrypt(s);
        }
        catch(Exception exception)
        {
            System.out.println("Error while decryption");
            throw new DecryptionNotSupportedException();
        }
        return s1;
    }
}