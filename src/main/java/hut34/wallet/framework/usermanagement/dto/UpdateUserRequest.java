package hut34.wallet.framework.usermanagement.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import hut34.wallet.framework.usermanagement.Role;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UpdateUserRequest {
    @NotBlank
    @Email
    private String email;
    private String name;
    private Set<Role> roles = new HashSet<>();

    public String getName() {
        return name;
    }

    public UpdateUserRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public UpdateUserRequest grantRoles(Role... roles) {
        return grantRoles(Arrays.asList(roles));
    }

    public UpdateUserRequest grantRoles(Collection<Role> roles) {
        getRoles().addAll(roles);
        return this;
    }

}
