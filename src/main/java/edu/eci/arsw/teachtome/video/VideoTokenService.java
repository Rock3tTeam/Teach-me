package edu.eci.arsw.teachtome.video;

public interface VideoTokenService {
    /**
     * Genera el token para utilizar Video
     *
     * @param jid Identificador del usuario
     * @return Un token para utilizar en el video
     */
    String generateProvisionToken(long jid);
}
