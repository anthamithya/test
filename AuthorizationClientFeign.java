package com.welcomeletterservice.service.impl;

import com.welcomeletterservice.service.response.CorrespondenceTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "authorization-client", url = "${correspondence.auth-server-url}")
public interface AuthorizationClientFeign {
    @PostMapping(value = "/realms/{realm}/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<CorrespondenceTokenResponse> getToken(@PathVariable("realm") String realm, @RequestBody Map<String, ?> formParams);

}
