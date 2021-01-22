package com.example.das_auth_providers.esia.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class EsiaPersonContacts {

    private Type contactType;
    private String value;
    private VerificationStatus verificationStatus;

    public enum Type {
        PHONE("OPH"),
        EMAIL("EML"),
        FAX("FAX");

        private final String name;

        Type(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum VerificationStatus {
        VERIFIED("S"),
        NOT_VERIFIED("N");

        private final String name;

        VerificationStatus(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
