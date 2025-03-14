package shopify.Services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import shopify.Data.Models.User;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Service for handling JWT token generation and decoding.
 */
@Service
public class TokenService {

    private final JwtDecoder decoder;

    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder, JwtDecoder decoder){
        this.decoder = decoder;
        this.encoder = encoder;
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token as a string.
     */
    public String generate(User user){
        Instant now = Instant.now();

        String scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(user.getUsername())
                .claim("roles", scope)
                .expiresAt(now.plusSeconds(3600))
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}