package com.jolupbisang.demo.infrastructure.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.auth.dto.OAuthUserInfoDto;
import com.jolupbisang.demo.application.auth.exception.AuthErrorCode;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.properties.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class OAuthClient {
    private final ObjectMapper objectMapper;
    private final OAuthProperties.Platform oAuthProperties;

    public String requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getAccessTokenParams(code), headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    oAuthProperties.getTokenUri(),
                    HttpMethod.POST,
                    request,
                    String.class
            );
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            throw new CustomException(AuthErrorCode.PLATFORM_ERROR);
        }
    }

    public OAuthUserInfoDto requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    oAuthProperties.getUserInfoUri(),
                    HttpMethod.GET,
                    request,
                    String.class
            );
            return parseUserInfo(objectMapper.readTree(response.getBody()));
        } catch (Exception e) {
            throw new CustomException(AuthErrorCode.PLATFORM_ERROR);
        }
    }

    protected abstract OAuthUserInfoDto parseUserInfo(JsonNode rootNode);

    private MultiValueMap<String, String> getAccessTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oAuthProperties.getClientId());
        params.add("client_secret", oAuthProperties.getClientSecret());
        params.add("redirect_uri", oAuthProperties.getRedirectUri());
        params.add("code", code);
        return params;
    }
}
