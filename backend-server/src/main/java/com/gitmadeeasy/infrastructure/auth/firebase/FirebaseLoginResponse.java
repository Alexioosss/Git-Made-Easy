package com.gitmadeeasy.infrastructure.auth.firebase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseLoginResponse {
    private String localId;
    private String email;
    private String idToken;
    private String refreshToken;
    private String expiresIn;

    public String getLocalId() { return localId; }
    public void setLocalId(String localId) { this.localId = localId; }
}