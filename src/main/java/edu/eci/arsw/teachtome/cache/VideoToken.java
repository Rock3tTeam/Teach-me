package edu.eci.arsw.teachtome.cache;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

public class VideoToken {
    public static final String PROVISION_TOKEN = "provision";
    private static final long EPOCH_SECONDS = 62167219200L;
    private static final String DELIM = "\0";
    private static final String TOKEN_DURATION = "86400";
    private static final String APP_ID = "2595ce.vidyo.io";
    private static final String KEY = "8b6d40c8a15a485b961d542937daf545";

    private VideoToken() {
    }

    public static String calculateExpiry() {
        long expiresLong;
        long currentUnixTimestamp = System.currentTimeMillis() / 1000;
        expiresLong = Long.parseLong(TOKEN_DURATION);
        return "" + (EPOCH_SECONDS + currentUnixTimestamp + expiresLong);
    }

    public static String generateProvisionToken(long jid) {
        String expires = calculateExpiry();
        String payload = String.join(DELIM, PROVISION_TOKEN, jid + "@" + APP_ID, expires, null);
        return new String(Base64.encodeBase64(
                (String.join(DELIM, payload, HmacUtils.hmacSha384Hex(KEY, payload))).getBytes()
        ));
    }
}
