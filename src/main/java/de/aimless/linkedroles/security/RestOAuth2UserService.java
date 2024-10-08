package de.aimless.linkedroles.security;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.client.RestOperations;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RestOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final RestOperations restOperations;
    private final String userAgent;

    public RestOAuth2UserService(RestOperations restOperations, String userAgent) {
        this.restOperations = restOperations;
        this.userAgent = userAgent;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String userInfoUrl = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userRequest.getAccessToken().getTokenValue()));
        headers.set(HttpHeaders.USER_AGENT, userAgent);

        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<Map<String, Object>> responseEntity = restOperations.exchange(userInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), typeReference);

        Map<String, Object> userInfoAttributes = responseEntity.getBody();
        Map<String, Object> userAttributes = (Map<String, Object>) userInfoAttributes.get("user");
        userInfoAttributes.put("username", userAttributes.get("username").toString());

        Set<GrantedAuthority> authorities = Collections.singleton(new OAuth2UserAuthority(userInfoAttributes));
        return new DefaultOAuth2User(authorities, userInfoAttributes, userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }
}