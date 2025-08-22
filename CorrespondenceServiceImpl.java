package com.welcomeletterservice.service.impl;

import com.welcomeletterservice.service.CorrespondenceService;
import com.welcomeletterservice.service.response.CorrespondenceResponse;
import com.welcomeletterservice.service.response.CorrespondenceTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CorrespondenceServiceImpl implements CorrespondenceService {

    @Value("${correspondence.realm-id}")
    private String correspondenceRealm;

    @Value("${correspondence.client-id}")
    private String correspondenceClientId;

    @Value("${correspondence.client-secret}")
    private String correspondenceClientSecret;

    private final AuthorizationService authorizationService;
    private final CorrespondenceClientFeign correspondenceClientFeign;

    public CorrespondenceServiceImpl(AuthorizationService authorizationService,
                                     CorrespondenceClientFeign correspondenceClientFeign) {
        this.authorizationService = authorizationService;
        this.correspondenceClientFeign = correspondenceClientFeign;
    }

    @Override
    public CorrespondenceResponse letterGeneration() {
        CorrespondenceTokenResponse tokenResponse = getAccessToken();
        String accessToken = tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken();
        System.out.println(accessToken);


        String accept = "application/vnd.ccm.v1+json";
        String clientId = "CCM - CCM service WEB";
        String correlationId = UUID.randomUUID().toString();
        Map<String, Object> requestBody = new HashMap<>();

        ResponseEntity<CorrespondenceResponse> response = correspondenceClientFeign.letterGeneration(
                accept,
                clientId,
                correlationId,
                accessToken,
                requestBody
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to generate letter: " + response.getStatusCode());
        }
    }

    private CorrespondenceTokenResponse getAccessToken() {
        return authorizationService.getCorrespondenceAccessToken(
                correspondenceRealm, correspondenceClientId, correspondenceClientSecret);
    }
}
