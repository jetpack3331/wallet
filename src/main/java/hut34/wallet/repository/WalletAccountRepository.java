package hut34.wallet.repository;

import hut34.wallet.model.WalletAccount;
import org.springframework.contrib.gae.objectify.ObjectifyProxy;
import org.springframework.contrib.gae.objectify.repository.base.BaseObjectifyStringRepository;
import org.springframework.contrib.gae.search.SearchService;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;

@Repository
public class WalletAccountRepository extends BaseObjectifyStringRepository<WalletAccount> {

    public WalletAccountRepository(ObjectifyProxy objectify, @Nullable SearchService searchService) {
        super(objectify, searchService, WalletAccount.class);
    }


}
