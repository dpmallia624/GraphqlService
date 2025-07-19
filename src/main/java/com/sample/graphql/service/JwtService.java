package com.sample.graphql.service;

import com.sample.graphql.configuration.IdentityConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtService {
    private final IdentityConfiguration identityConfiguration;

    public JwtService(IdentityConfiguration identityConfiguration) {
        this.identityConfiguration = identityConfiguration;
    }

    public boolean validateToken(String token) {
        try {
            String jwksUrl = identityConfiguration.getUrl()+"/.well-known/openid-configuration/jwks";

            // Retrieve the JWKS from the specified URL
            JWKSet jwkSet = JWKSet.load(new URL(jwksUrl));

            // Parse the JWT token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Get the JWS Header from the parsed token
            JWSHeader jwsHeader = signedJWT.getHeader();

            // Find the JWK that matches the Key ID (kid) in the JWS Header
            RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(jwsHeader.getKeyID());

            // Verify the signature using the RSA public key
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) rsaKey.toPublicKey());
            if (!signedJWT.verify(verifier)) {
                return false; // Invalid signature
            }

            // Extract and validate the JWT claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Check token expiration
            Date expirationTime = claims.getExpirationTime();
            Date currentTime = new Date();
            long expirationThreshold = 50 * 60 * 60 * 1000; // 1 hour in milliseconds
            if ((expirationTime != null && expirationTime.before(currentTime))) return false;
            assert expirationTime != null;
            // Token has expired or is not within 1 hour
            return expirationTime.getTime() - currentTime.getTime() <= expirationThreshold;

            // Perform additional validation if needed
            // For example, check the token expiration, issuer, or custom claims

        } catch (Exception e) {
            return false; // Token is invalid
        }
    }

    public Authentication getAuthentication(String token) {

        try {

        // Parse the JWT token
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        // 1. Parse token to extract username and roles
        String username = claims.getSubject();

        // 2. Convert roles to GrantedAuthority
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER", "WRITE_PRIVILEGE");

        // 3. Return Authentication object
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (Exception e) {
            return null; // Token is invalid
        }
    }
}

