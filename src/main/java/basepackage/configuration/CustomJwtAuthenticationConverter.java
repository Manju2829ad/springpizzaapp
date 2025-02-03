package basepackage.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import basepackage.util.JwtTokenUtil;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtTokenUtil jwtTokenUtil;

    public CustomJwtAuthenticationConverter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String token = jwt.getTokenValue();
        if (jwtTokenUtil.validateToken(token)) {
            // Extract roles or authorities from the JWT token and return as GrantedAuthority
            return jwt.getClaimAsStringList("roles").stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return null; // If the token is invalid, no authorities are returned
    }
}
