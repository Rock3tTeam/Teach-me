/*package edu.eci.arsw.teachtome.JWT;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        System.out.println(request.getRequestURL());
        if(authorizationHeader==null || authorizationHeader.isEmpty()){
            throw new ServletException("Token nulo");
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(),"");
        try{
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            String username = body.getSubject();
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e){
            throw new IllegalStateException("Token %s cannot be truest "+token);
        }
        filterChain.doFilter(request, response);
    }
}


 */