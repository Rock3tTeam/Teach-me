package edu.eci.arsw.teachtome.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenHelper {

    public static final long TOKEN_TIMEOUT_IN_MILLIS = 14400000;
    private static final String PREFIX = "TToken ";
    private static final String SECRET = "python123";
    private static final String TOKEN_ID = "TeachToMe";

    public static String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId(TOKEN_ID)
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIMEOUT_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS512,
                        SECRET.getBytes()).compact();

        return PREFIX + token;
    }
}
