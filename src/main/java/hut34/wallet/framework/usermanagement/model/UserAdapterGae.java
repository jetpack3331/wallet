package hut34.wallet.framework.usermanagement.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import hut34.wallet.framework.usermanagement.Role;
import hut34.wallet.framework.usermanagement.dto.AuthUser;
import hut34.wallet.framework.usermanagement.service.UserService;
import hut34.wallet.util.Assert;
import org.springframework.contrib.gae.security.UserAdapter;
import org.springframework.contrib.gae.util.Nulls;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserAdapterGae implements UserAdapter<User> {

    private final boolean byEmail;
    private final UserService userService;

    private UserAdapterGae(UserService userService, boolean byEmail) {
        this.userService = userService;
        this.byEmail = byEmail;
    }

    public static UserAdapterGae byEmail(UserService userService) {
        return new UserAdapterGae(userService, true);
    }

    public static UserAdapterGae byUsername(UserService userService) {
        return new UserAdapterGae(userService, false);
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

    @SuppressWarnings("ConstantConditions")
    public User getCurrentUserRequired() {
        Optional<User> currentUser = getCurrentUser();
        return Assert.isPresent(currentUser, "Authenticated user required")
            .get();
    }

    public Optional<User> getCurrentUser() {
        return currentUserId()
            .flatMap(userService::getById);
    }

    public static Optional<Ref<User>> currentUserRef() {
        return currentUserKey()
            .map(Ref::create);
    }

    public static Optional<Key<User>> currentUserKey() {
        return currentUserId()
            .map(id -> Key.create(User.class, id));
    }

    private static Optional<String> currentUserId() {
        Object principal = Nulls.ifNotNull(SecurityContextHolder.getContext().getAuthentication(), auth -> auth.getPrincipal());
        if (principal instanceof AuthUser) {
            return Optional.of(((AuthUser) principal).getId());
        }
        return Optional.empty();
    }

    private List<Role> transformToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(Role::valueOf)
            .collect(Collectors.toList());
    }
}
