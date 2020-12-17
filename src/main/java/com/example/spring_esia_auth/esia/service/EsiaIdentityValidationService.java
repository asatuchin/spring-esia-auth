package com.example.spring_esia_auth.esia.service;

import com.example.spring_esia_auth.exception.JwtParsingException;
import com.example.spring_esia_auth.esia.model.EsiaJwtHeader;
import com.example.spring_esia_auth.esia.model.EsiaJwtPayload;
import com.example.spring_esia_auth.esia.model.EsiaOAuth2TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;

@Service
@AllArgsConstructor
public class EsiaIdentityValidationService {

    @Value("${esia.jwt.issuer}")
    final String validTokenIssuer;

    @Value("${esia.jwt.client}")
    final String validTokenClient;

    private final EsiaCertificateStorage certStorage;
    private final ObjectMapper objectMapper;

    public void validateIdentityToken(final EsiaOAuth2TokenResponse token) throws JwtValidationException {
        try {
            EsiaJwtPayload payload = getPayload(token.getIdToken());

            // Validate token expiration date
            if (payload.getExp() > Instant.now().getEpochSecond()) {
                throw new JwtValidationException("ESIA token expired", Collections.emptyList());
            }

            // Validate that token client ID matches with a client ID of current system
            if (!payload.getClientId().equals(validTokenClient)) {
                throw new JwtValidationException("Invalid token client", Collections.emptyList());
            }

            // Validate that token issuer matches with mnemonic of ESIA
            if (!payload.getIss().equals(validTokenIssuer)) {
                throw new JwtValidationException("Invalid token client", Collections.emptyList());
            }

            if (!verifyTokenSignature(token.getIdToken())) {
                throw new JwtValidationException("Invalid token signature", Collections.emptyList());
            }
        } catch (JwtParsingException e) {
            throw new JwtValidationException("Failed", Collections.emptyList());
        }
    }

    private boolean verifyTokenSignature(final String token) throws JwtParsingException {
        String[] tokenParts = token.split("\\.");

        byte[] data = concat(Base64.getDecoder().decode(tokenParts[0]), Base64.getDecoder().decode(tokenParts[1]));
        byte[] signature = Base64.getDecoder().decode(tokenParts[2]);

        Security.addProvider(new BouncyCastleProvider());

        try {
            CMSSignedData s = new CMSSignedData(new CMSProcessableByteArray(data), signature);
            SignerInformationStore signers = s.getSignerInfos();
            SignerInformation signerInfo = signers.getSigners().iterator().next();

            EsiaJwtHeader header = getHeader(token);
            X509Certificate cert = certStorage.get(header.getAlg());

            return signerInfo.verify(
                    new JcaSimpleSignerInfoVerifierBuilder()
                            .build(cert.getPublicKey())
            );
        } catch (OperatorCreationException | CMSException e) {
            throw new JwtParsingException("Failed ESIA jwt signature verification", e);
        }
    }

    private EsiaJwtHeader getHeader(final String token) throws JwtParsingException {
        try {
            String headerString = new String(
                    Base64.getDecoder().decode(token.split("\\.")[0]),
                    StandardCharsets.UTF_8
            );
            return objectMapper.readValue(headerString, EsiaJwtHeader.class);
        } catch (JsonProcessingException e) {
            throw new JwtParsingException("Failed json header parsing", e);
        }
    }

    private EsiaJwtPayload getPayload(final String token) throws JwtParsingException {
        try {
            String payloadString = new String(
                    Base64.getDecoder().decode(token.split("\\.")[1]),
                    StandardCharsets.UTF_8
            );
            return objectMapper.readValue(payloadString, EsiaJwtPayload.class);
        } catch (JsonProcessingException e) {
            throw new JwtParsingException("Failed json payload parsing", e);
        }
    }

    private byte[] concat(final byte[] a, final byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
