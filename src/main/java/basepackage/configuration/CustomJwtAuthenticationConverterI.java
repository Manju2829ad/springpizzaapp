package basepackage.configuration;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.fasterxml.jackson.databind.util.Converter;

import io.jsonwebtoken.Jwt;

public interface CustomJwtAuthenticationConverterI extends Converter<Jwt, AbstractAuthenticationToken> {
}