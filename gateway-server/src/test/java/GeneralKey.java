import com.google.common.io.BaseEncoding;

import java.math.BigInteger;
import java.security.SecureRandom;

public class GeneralKey {
    public static void main(String[] args) {
        String result = BaseEncoding.base64Url().encode(new BigInteger(384, new SecureRandom()).toByteArray());
        System.out.println(result);
    }
}
