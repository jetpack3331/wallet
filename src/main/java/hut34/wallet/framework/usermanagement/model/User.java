package hut34.wallet.framework.usermanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import hut34.wallet.framework.BaseEntityCore;
import hut34.wallet.framework.usermanagement.Role;
import hut34.wallet.util.PasswordGenerator;
import org.springframework.contrib.gae.security.GaeUser;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Cache
@Entity
public class User extends BaseEntityCore implements GaeUser, Serializable {
    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private Set<Role> roles = new LinkedHashSet<>();
    private boolean enabled = true;

    private String externalId;
    private AuthProvider provider = AuthProvider.INTERNAL;
    private OffsetDateTime termsAccepted;

    private User() {
    }

    public static User byEmail(String email, String password) {
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.email = email;
        user.password = password;
        return user;
    }

    public static User invitedByEmail(String email) {
        User user = new User();
        user.id = UUID.randomUUID().toString();
        user.email = email;
        user.enabled = false;
        return user;
    }

    public static User byUsername(String username, String password) {
        User user = new User();
        user.id = username;
        user.password = password;
        return user;
    }

    public static User byProviderEmail(AuthProvider provider, String externalId, String email) {
        User user = byEmail(email, null);
        user.setProvider(provider);
        user.setExternalId(externalId);
        user.setPassword(PasswordGenerator.alphanumeric(24));
        return user;
    }

    public String getId() {
        return id;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public User setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public User setProvider(AuthProvider provider) {
        this.provider = provider;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Collection<Role> roles) {
        this.roles = new LinkedHashSet<>(roles);
        return this;
    }

    public User grantRoles(Role... role) {
        return grantRoles(Arrays.asList(role));
    }

    public User grantRoles(Collection<Role> roles) {
        this.roles.addAll(roles);
        return this;
    }

    public User revokeRoles(Role... role) {
        return revokeRoles(Arrays.asList(role));
    }

    public User revokeRoles(Collection<Role> roles) {
        this.roles.removeAll(roles);
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public OffsetDateTime getTermsAccepted() {
        return termsAccepted;
    }

    public User setTermsAccepted(OffsetDateTime termsAccepted) {
        this.termsAccepted = termsAccepted;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
