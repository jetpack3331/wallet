package hut34.wallet.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import hut34.wallet.framework.BaseEntity;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.util.Assert;
import org.springframework.contrib.gae.objectify.Refs;

import java.util.Objects;

import static hut34.wallet.model.WalletAccountType.PRIVATE;

@Entity
public class WalletAccount extends BaseEntity {
    public static class Fields {
        public static final String owner = "owner";
    }

    @Id
    private String address;
    @Index
    private Ref<User> owner;
    @Index
    private WalletAccountType type;
    private String secretStorageJson;

    private WalletAccount() {
        this.type = PRIVATE;
    }

    public WalletAccount(WalletAccountType type, String address, User owner) {
        // TODO: see how to integrate JSR validation annotations
        Assert.notNull(type, "type required");
        Assert.notBlank(address, "address required");
        Assert.notNull(owner, "owner required");
        this.type = type;
        this.address = address;
        this.owner = Refs.ref(owner);
    }

    public String getAddress() {
        return address;
    }

    public String getSecretStorageJson() {
        return secretStorageJson;
    }

    public WalletAccount setSecretStorageJson(String secretStorageJson) {
        this.secretStorageJson = secretStorageJson;
        return this;
    }

    public WalletAccountType getType() {
        return type;
    }

    public WalletAccount setType(WalletAccountType type) {
        this.type = type;
        return this;
    }

    public User getOwner() {
        return Refs.deref(owner);
    }

    public Key<User> getOwnerKey() {
        return owner.getKey();
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
