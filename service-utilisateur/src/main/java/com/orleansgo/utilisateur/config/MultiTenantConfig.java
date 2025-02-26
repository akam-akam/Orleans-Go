package com.orleansgo.utilisateur.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .map(attrs -> (String) attrs.getAttribute("TENANT_ID", RequestAttributes.SCOPE_REQUEST))
            .orElse(DEFAULT_TENANT);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
