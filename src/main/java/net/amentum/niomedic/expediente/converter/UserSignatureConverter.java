package net.amentum.niomedic.expediente.converter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class UserSignatureConverter {
    public String toStringFromByte(byte[] signatureContent) {
        return Base64.encodeBase64String(signatureContent);
    }
}
