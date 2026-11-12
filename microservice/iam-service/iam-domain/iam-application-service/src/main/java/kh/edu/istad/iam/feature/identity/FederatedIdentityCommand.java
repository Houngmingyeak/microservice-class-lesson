package kh.edu.istad.iam.feature.identity;

import java.util.Map;

public record FederatedIdentityCommand(
        String provider,
        String providerSubject,
        String email,
        Boolean emailVerified,
        String displayName,
        String username,
        String pictureUrl,
        Map<String, Object> attributes
) {
}

