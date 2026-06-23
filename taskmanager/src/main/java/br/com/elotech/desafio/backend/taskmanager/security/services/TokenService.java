package br.com.elotech.desafio.backend.taskmanager.security.services;

import br.com.elotech.desafio.backend.taskmanager.exceptions.UnauthorizedException;
import br.com.elotech.desafio.backend.taskmanager.security.responses.TokenResponse;
import br.com.elotech.desafio.backend.taskmanager.security.dtos.gets.UserLoginValidationGetDTO;
import br.com.elotech.desafio.backend.taskmanager.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TokenService {

    private static final String ISSUER = "auth-service";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private final MessageUtils messageUtils;

    public TokenService(JwtEncoder jwtEncoder, @Qualifier("refreshJwtDecoder") JwtDecoder jwtDecoder, MessageUtils messageUtils) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.messageUtils = messageUtils;
    }

    public TokenResponse generateTokenResponse(UserLoginValidationGetDTO userLoginValidationGetDTO) {
        return new TokenResponse(generateToken(userLoginValidationGetDTO), generateRefreshToken(userLoginValidationGetDTO));
    }

    public UUID validateRefreshTokenEGetUserId(String refreshToken) {
        Jwt jwt = decodeToken(refreshToken);
        validateTokenType(jwt, REFRESH_TOKEN_TYPE);

        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException(messageUtils.getMessage("user.refresh-token.invalid"));
        }
    }

    private String generateToken(UserLoginValidationGetDTO userLoginValidationGetDTO) {
        return generateToken(userLoginValidationGetDTO, ACCESS_TOKEN_TYPE, 15, ChronoUnit.MINUTES);
    }

    private String generateRefreshToken(UserLoginValidationGetDTO userLoginValidationGetDTO) {
        return generateToken(userLoginValidationGetDTO, REFRESH_TOKEN_TYPE, 15, ChronoUnit.DAYS);
    }

    private String generateToken(UserLoginValidationGetDTO userLoginValidationGetDTO, String tokenType, long amountToAdd, ChronoUnit chronoUnit) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plus(amountToAdd, chronoUnit))
                .subject(userLoginValidationGetDTO.id().toString())
                .claim("roles", userLoginValidationGetDTO.role())
                .claim("token_type", tokenType)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    private Jwt decodeToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (Exception exception) {
            throw new UnauthorizedException(messageUtils.getMessage("user.refresh-token.invalid"));
        }
    }

    private void validateTokenType(Jwt jwt, String expectedTokenType) {
        String tokenType = jwt.getClaimAsString("token_type");
        if (!expectedTokenType.equals(tokenType)) {
            throw new UnauthorizedException(messageUtils.getMessage("user.refresh-token.invalid"));
        }
    }
}
