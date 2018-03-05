package hut34.wallet.framework.usermanagement;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role implements GrantedAuthority {
    SUPER,
    ADMIN,
    USER;

    public static Role valueOf(GrantedAuthority authority) {
        String role = authority.getAuthority().replace("ROLE_", "");
        return valueOf(role);
    }

    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", this.name());
    }

    public static Set<Role> parseSet(Set<String> stringRoles) {
        return stringRoles.stream().map(s -> Role.valueOf(s.trim().toUpperCase())).collect(Collectors.toSet());
    }
}
