package ws.academy.auction.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    ADMIN("Администратор"),
    PARTICIPANT("Участник");

    private final String description;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
