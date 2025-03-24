package com.jolupbisang.demo.infrastructure.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.auth.dto.OAuthUserInfoDto;
import com.jolupbisang.demo.global.properties.OAuthProperties;
import org.springframework.stereotype.Component;

@Component
public class GoogleClient extends OAuthClient {

    public GoogleClient(ObjectMapper objectMapper, OAuthProperties oAuthProperties) {
        super(objectMapper, oAuthProperties.getGoogle());
    }

    @Override
    protected OAuthUserInfoDto parseUserInfo(JsonNode rootNode) {
        String id = rootNode.path("id").asText();
        String email = rootNode.path("email").asText();
        String name = rootNode.path("name").asText();
        return OAuthUserInfoDto.of(id, email, name);
    }
}
