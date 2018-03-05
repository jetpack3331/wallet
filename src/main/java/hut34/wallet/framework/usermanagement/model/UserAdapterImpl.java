package hut34.wallet.framework.usermanagement.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import org.springframework.contrib.gae.security.UserAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import hut34.wallet.framework.usermanagement.Role;
import hut34.wallet.framework.usermanagement.dto.AuthUser;
import hut34.wallet.framework.usermanagement.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserAdapterImpl implements UserAdapter<User> {

    private final boolean byEmail;
    private final UserService userService;

    private UserAdapterImpl(UserService userService, boolean byEmail) {
        this.userService = userService;
        this.byEmail = byEmail;
    }

    public static UserAdapterImpl byEmail(UserService userService) {
        return new UserAdapterImpl(userService, true);
    }

    public static UserAdapterImpl byUsername(UserService userService) {
        return new UserAdapterImpl(userService, false);
    }

    @Override
    public User newFromUserDetails(UserDetails userDetails) {
        User user = byEmail ?
            User.byEmail(userDetails.getUsername(), userDetails.getPassword()) :
            User.byUsername(userDetails.getUsername(), userDetails.getPassword());

        user.setRoles(transformToRoles(userDetails.getAuthorities()));
        user.setEnabled(userDetails.isEnabled());
        return user;
    }

    @Override
    public void mergeUserDetails(User user, UserDetails userDetails) {
        List<Role> roles = transformToRoles(userDetails.getAuthorities());
        user.setRoles(roles);
        user.setPassword(userDetails.getPassword());
        user.setEnabled(userDetails.isEnabled());
    }

    @Override
    public void setPassword(User user, String newPassword) {
        user.setPassword(newPassword);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles();
    }

    @Override
    public UserDetails toUserDetails(User user) {
        return AuthUser.withId(user.getId())
            .username(byEmail ? user.getEmail() : user.getId())
            .password(user.getPassword())
            .authorities(new ArrayList<>(user.getRoles()))
            .disabled(!user.isEnabled())
            .build();
    }

    @Override
    public Optional<Key<User>> getUserKey(String username, Class<User> userClass) {
        return userService.get(username)
            .map(Key::create);
    }

    public static Optional<Ref<User>> currentUserRef() {
        return currentUserKey()
            .map(Ref::create);
    }

    public static Optional<Key<User>> currentUserKey() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthUser) {
            return Optional.of(Key.create(User.class, ((AuthUser) principal).getId()));
        }
        return Optional.empty();
    }

    private List<Role> transformToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(Role::valueOf)
            .collect(Collectors.toList());
    }
}
