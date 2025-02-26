package com.orleansgo.utilisateur.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oauthUser = super.loadUser(request);

        Map<String, Object> attributes = oauthUser.getAttributes();
        String provider = request.getClientRegistration().getRegistrationId();

        return new CustomOAuth2User(attributes, provider);
    }
}

public class CustomOAuth2User implements OAuth2User {

    private final Map<String, Object> attributes;
    private final String provider;

    public CustomOAuth2User(Map<String, Object> attributes, String provider) {
        this.attributes = attributes;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return (String) attributes.get("sub");
    }

    public String getProvider() {
        return provider;
    }
}
