package com.welcomeletterservice.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.welcomeletterservice.service.response.CorrespondenceTokenResponse;
import feign.FeignException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final String ACCESS_TOKENS = "accessTokens";
    private final String CLIENT_ID = "client_id";
    private final String CLIENT_SECRET = "client_secret";
    private final String GRANT_TYPE = "grant_type";
    private final String REALM = "realm";

    private final AuthorizationClientFeign authorizationClientFeign;
    private final CacheManager cacheManager;
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    public AuthorizationService(AuthorizationClientFeign authorizationClientFeign,
                                CacheManager cacheManager) {
        this.authorizationClientFeign = authorizationClientFeign;
        this.cacheManager = cacheManager;
    }

    private CorrespondenceTokenResponse getAccessToken(Map<String, String> params) {
        try {
            ResponseEntity<CorrespondenceTokenResponse> response = authorizationClientFeign.getToken(params.get("realm"), params);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new KeycloakInvalidCredentialsException("Failed to fetch access token: " + response.getStatusCode());
        } catch (FeignException e) {
            throw new RuntimeException("Error calling Keycloak token endpoint", e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public CorrespondenceTokenResponse getCorrespondenceAccessToken(String realm,
                                                                    String clientId,
                                                                    String clientSecret) {
        Cache cache = cacheManager.getCache(ACCESS_TOKENS);
        if (cache == null) {
            Map<String, String> params = buildTokenParams(realm, clientId, clientSecret);
            return getAccessToken(params);
        }
        CorrespondenceTokenResponse cached = cache.get(clientId, CorrespondenceTokenResponse.class);
        if (cached != null && !cached.isExpired()) {
            return cached;
        }
        Object lock = locks.computeIfAbsent(clientId, k -> new Object());
        synchronized (lock) {
            cached = cache.get(clientId, CorrespondenceTokenResponse.class);
            if (cached != null && !cached.isExpired()) {
                return cached;
            }
            if (cached != null) {
                cache.evict(clientId);
            }
            Map<String, String> params = buildTokenParams(realm, clientId, clientSecret);
            CorrespondenceTokenResponse newToken = getAccessToken(params);
            cache.put(clientId, newToken);
            return newToken;
        }
    }

    private Map<String, String> buildTokenParams(String realm, String clientId, String clientSecret) {
        Map<String, String> params = new HashMap<>();
        params.put(CLIENT_ID, clientId);
        params.put(CLIENT_SECRET, clientSecret);
        params.put(GRANT_TYPE, "client_credentials");
        params.put(REALM, realm);
        return params;
    }

    private static class KeycloakInvalidCredentialsException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private KeycloakInvalidCredentialsException(String message) {
            super(message);
        }
    }

}