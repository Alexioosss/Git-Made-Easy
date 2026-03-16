package com.gitmadeeasy.infrastructure.auth.firebase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO class for mapping the Firebase response when logging a user in via email and password through Firebase's Authentication system
 * The data that the response returns, and more information, have been, and can be found in firebase's original documentation
 * Direct Link: <a href="https://firebase.google.com/docs/reference/rest/auth/#section-sign-in-email-password">...</a>
 */
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