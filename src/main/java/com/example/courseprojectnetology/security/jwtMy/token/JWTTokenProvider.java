package com.example.courseprojectnetology.security.jwtMy.token;

import com.example.courseprojectnetology.exception.errors.BadRequestError;
import com.example.courseprojectnetology.models.Role;
import com.example.courseprojectnetology.service.Impl.ExceptionSingletonServiceImpl;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JWTTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;


    private UserDetailsService userDetailsService;

    @Autowired
    public JWTTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }


    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, List<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secret)//
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("auth-token");
        if (bearerToken != null) {
            return bearerToken;
        }
        return null;
    }

    public String refreshToken(String token) {
        System.out.println();
        Date dateExp = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        Date dateNow = new Date();
        Date validity = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getIssuedAt();
        Claims c = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().setExpiration(validity);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().toString();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Date date = claims.getBody().getExpiration();
            if (date.before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException e) {
            throw new BadRequestError("JwtException", ExceptionSingletonServiceImpl.getInstance().getId());
        } catch (IllegalArgumentException e) {
            throw new BadRequestError("IllegalArgumentException", ExceptionSingletonServiceImpl.getInstance().getId());
        }
    }

    private List<String> getRoleNames(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }
}
