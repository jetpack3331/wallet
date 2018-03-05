package hut34.wallet.framework.usermanagement.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.contrib.gae.objectify.Refs;
import hut34.wallet.util.DateTimeUtils;

import java.time.OffsetDateTime;

@Entity
public class LoginIdentifier {
    @Id
    private String loginIdentifier;
    private OffsetDateTime created;
    private Ref<User> user;

    // For Objectify
    private LoginIdentifier() {
    }

    public LoginIdentifier(User user) {
        this.loginIdentifier = user.getEmail();
        this.created = DateTimeUtils.now();
        this.user = Ref.create(user);
    }

    public String getLoginIdentifier() {
        return loginIdentifier;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public User getUser() {
        return Refs.deref(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LoginIdentifier that = (LoginIdentifier) o;

        return new EqualsBuilder()
            .append(loginIdentifier, that.loginIdentifier)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(loginIdentifier)
            .toHashCode();
    }
}
