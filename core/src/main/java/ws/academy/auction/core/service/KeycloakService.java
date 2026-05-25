package ws.academy.auction.core.service;

import ws.academy.auction.api.dto.rs.users.JwtRs;

/**
 * Сервис по управлению Keycloak'ом
 */
public interface KeycloakService {
    /**
     * Создание пользователя в Keycloak
     *
     * @param username - имя пользователя
     * @param email - email-адресс пользователя
     * @param password - пароль пользователя
     * @return String - UUID созданный keycloak'ом в виде строки
     */
    String createUser(String username, String email, String password);

    /**
     * Аутентификация пользователя с помощью Keycloak
     *
     * @param login - логин пользователя (его email-адрес)
     * @param password - пароль пользователя
     * @return JwtRs объект - dto-объект хранящий в себе accessToken и refreshToken
     */
    JwtRs authenticatedUser(String login, String password);

    /**
     * Обновление токена пользователя
     *
     * @param refreshToken - refreshToken пользователя
     * @return JwtRs объект - dto-объект хранящий в себе accessToken и refreshToken
     */
    JwtRs refreshTokenUser(String refreshToken);

    /**
     * Обновление пароля пользователя
     *
     * @param guidKeycloak - идентификатор пользователя в Keycloak
     * @param newPassword - новый пароль пользователя
     */
    void resetPassword(String guidKeycloak, String newPassword);
}
