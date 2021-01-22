package com.example.das_auth_providers.esia.service;

import com.example.das_auth_providers.esia.exception.EsiaSignatureException;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class EsiaSignatureService {

    private final static String ALGORITHM = "PKCS12";
    private static final String SIGNATURE_ALGORITHM = "Sha1WithRSA";

    private final KeyStore keyStore;
    private final PrivateKey privateKey;
    private final X509Certificate certificate;

    public EsiaSignatureService(final String keyPairFilePath, final String storePassword) throws EsiaSignatureException {
        try {
            this.keyStore = KeyStore.getInstance(ALGORITHM);
            keyStore.load(
                    getClass().getClassLoader().getResourceAsStream(keyPairFilePath),
                    storePassword.toCharArray()
            );

            Enumeration<String> aliases = keyStore.aliases();
            String aliaz = "";
            while (aliases.hasMoreElements()) {
                aliaz = aliases.nextElement();
                if (keyStore.isKeyEntry(aliaz)) {
                    break;
                }
            }

            privateKey = (PrivateKey) keyStore.getKey(aliaz, storePassword.toCharArray());
            certificate = (X509Certificate) keyStore.getCertificate(aliaz);
        } catch (Exception e) {
            throw new EsiaSignatureException("Failed to initialize a service", e);
        }
    }

    public String sign(final String message) throws EsiaSignatureException {

        try {
            byte[] dataToSign = message.getBytes();

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(dataToSign);
            byte[] signedData = signature.sign();

            X500Name xName = X500Name.asX500Name(certificate.getSubjectX500Principal());
            BigInteger serial = certificate.getSerialNumber();
            AlgorithmId digestAlgorithmId = new AlgorithmId(AlgorithmId.SHA_oid);
            AlgorithmId signAlgorithmId = new AlgorithmId(AlgorithmId.RSAEncryption_oid);

            SignerInfo sInfo = new SignerInfo(xName, serial, digestAlgorithmId, signAlgorithmId, signedData);
            ContentInfo cInfo = new ContentInfo(
                    ContentInfo.DIGESTED_DATA_OID,
                    new DerValue(DerValue.tag_OctetString, dataToSign)
            );
            PKCS7 p7 = new PKCS7(
                    new AlgorithmId[]{digestAlgorithmId},
                    cInfo,
                    new X509Certificate[]{certificate},
                    new SignerInfo[]{sInfo});

            ByteArrayOutputStream bOut = new DerOutputStream();
            p7.encodeSignedData(bOut);
            return bOut.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new EsiaSignatureException("Unknown algorithm", e);
        } catch (IOException e) {
            throw new EsiaSignatureException("Failed to encode signed data", e);
        } catch (SignatureException e) {
            throw new EsiaSignatureException("Failed to sign data", e);
        } catch (InvalidKeyException e) {
            throw new EsiaSignatureException("Key is invalid", e);
        }
    }
}
