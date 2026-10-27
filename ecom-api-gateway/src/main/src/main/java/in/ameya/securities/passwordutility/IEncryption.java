package in.ameya.securities.passwordutility;

import org.springframework.stereotype.Component;

// Referenced classes of package utils:
//            DecryptionNotSupportedException

@Component
public interface IEncryption
{

    public abstract String crypt(String s);

    public abstract boolean matches(String s, String s1);

    public abstract String decrypt(String s)
        throws DecryptionNotSupportedException;
}