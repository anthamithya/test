package com.welcomeletterservice.service.impl;

import com.welcomeletterservice.service.response.CorrespondenceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "correspondence-client", url = "${correspondence.api-base-url}")
public interface CorrespondenceClientFeign {
    @PostMapping(
            value = "/letter-generation/by-template",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<CorrespondenceResponse> letterGeneration(
            @RequestHeader("Accept") String accept,
            @RequestHeader("x-client-id") String clientId,
            @RequestHeader("x-corelation-id") String correlationId,
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody Map<String, Object> requestBody
    );

}
