package in.ameya.securities.passwordutility;

import org.springframework.stereotype.Component;

// Referenced classes of package utils:
//            DecryptionNotSupportedException, IEncryption

@Component
public class DefaultEncryption
    implements IEncryption
{

    public DefaultEncryption()
    {
    }

    public String crypt(String s)
    {
        return s;
    }

    public boolean matches(String s, String s1)
    {
        return false;
    }

    public String decrypt(String s)
        throws DecryptionNotSupportedException
    {
        throw new DecryptionNotSupportedException();
    }
}