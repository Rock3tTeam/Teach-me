package edu.eci.arsw.teachtome.video;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

@Service
public class VideoTokenImpl implements VideoTokenService {
    public static final String PROVISION_TOKEN = "provision";
    private static final long EPOCH_SECONDS = 62167219200L;
    private static final String DELIM = "\0";
    private static final String TOKEN_DURATION = "86400";
    private static final String APP_ID = "2595ce.vidyo.io";
    private static final String KEY = "8b6d40c8a15a485b961d542937daf545";

    private VideoTokenImpl() {
    }

    /**
     * Genera el token para utilizar Video
     *
     * @param jid Identificador del usuario
     * @return Un token para utilizar en el video
     */
    @Override
    public String generateProvisionToken(long jid) {
        String expires = calculateExpiry();
        String payload = String.join(DELIM, PROVISION_TOKEN, jid + "@" + APP_ID, expires, null);
        return new String(Base64.encodeBase64(
                (String.join(DELIM, payload, HmacUtils.hmacSha384Hex(KEY, payload))).getBytes()
        ));
    }

    /**
     * Calcula la duracion del token del video
     *
     * @return Una cadena de caracteres con duracion del token en milisegundos.
     */
    private String calculateExpiry() {
        long expiresLong;
        long currentUnixTimestamp = System.currentTimeMillis() / 1000;
        expiresLong = Long.parseLong(TOKEN_DURATION);
        return "" + (EPOCH_SECONDS + currentUnixTimestamp + expiresLong);
    }
}
