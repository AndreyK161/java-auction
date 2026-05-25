package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ws.academy.auction.api.dto.rs.users.JwtRs;
import ws.academy.auction.core.service.KeycloakService;

import javax.ws.rs.core.Response;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final Keycloak keycloak;
    private final String keycloakRealm;

    @Value("${keycloak.url-authenticate}")
    private String urlAuthenticatedKeycloak;

    @Override
    public String createUser(String username, String email, String password) {
        UserRepresentation user = createUserRepresentation(username, email);
        String userId = createUserInKeycloak(user);
        setUserPassword(userId, password);
        return userId;
    }

    @Override
    public JwtRs authenticatedUser(String login, String password) {
        HttpEntity<MultiValueMap<String, String>> request = buildLoginRequest(login, password);
        Map<String, String> tokenResponse = callTokenEndpoint(request);
        return new JwtRs(tokenResponse.get("access_token"), tokenResponse.get("refresh_token"));
    }

    @Override
    public JwtRs refreshTokenUser(String refreshToken) {
        HttpEntity<MultiValueMap<String, String>> request = buildRefreshTokenRequest(refreshToken);
        Map<String, String> tokenResponse = callTokenEndpoint(request);
        return new JwtRs(tokenResponse.get("access_token"), tokenResponse.get("refresh_token"));
    }

    @Override
    public void resetPassword(String guidKeycloak, String newPassword) {
        setUserPassword(guidKeycloak, newPassword);
    }

    private Map<String, String> callTokenEndpoint(HttpEntity<MultiValueMap<String, String>> request) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<Map<String, String>> response = template.exchange(
                urlAuthenticatedKeycloak,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> buildLoginRequest(String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "auction-client");
        form.add("username", login);
        form.add("password", password);
        return new HttpEntity<>(form, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> buildRefreshTokenRequest(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", "auction-client");
        form.add("refresh_token", refreshToken);
        return new HttpEntity<>(form, headers);
    }

    private UserRepresentation createUserRepresentation(String username, String email) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        return user;
    }

    private String createUserInKeycloak(UserRepresentation user) {
        Response response = keycloak.realm(keycloakRealm).users().create(user);

        if (response.getStatus() != 201) {
            throw new IllegalStateException("Не удалось создать пользователя в Keycloak: " + response.getStatus());
        }

        String[] pathSegments = response.getLocation().getPath().split("/");
        return pathSegments[pathSegments.length - 1];
    }

    private void setUserPassword(String userId, String password) {
        UserResource userResource = keycloak.realm(keycloakRealm).users().get(userId);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);

        userResource.resetPassword(cred);
    }
}
