package hut34.wallet.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.util.Assert;
import org.springframework.contrib.gae.objectify.Refs;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class WalletAccount extends BaseEntity {
    @NotNull
    @Id
    private String address;
    @NotNull
    private Ref<User> owner;
    private String encryptedPrivateKey;

    public WalletAccount(String address, User owner) {
        // TODO: see how to integrate JSR validation annotations
        Assert.notBlank(address, "address required");
        Assert.notNull(owner, "owner required");

        this.address = address;
        this.owner = Refs.ref(owner);
    }

    public String getAddress() {
        return address;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public WalletAccount setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
        return this;
    }

    public User getOwner() {
        return Refs.deref(owner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletAccount that = (WalletAccount) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

}
