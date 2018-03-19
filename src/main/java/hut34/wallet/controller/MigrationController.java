package hut34.wallet.controller;

import hut34.wallet.model.WalletAccount;
import hut34.wallet.model.WalletAccountType;
import hut34.wallet.repository.WalletAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/migrations")
public class MigrationController {

    private static final Logger LOG = LoggerFactory.getLogger(MigrationController.class);

    private WalletAccountRepository walletAccountRepository;

    public MigrationController(WalletAccountRepository walletAccountRepository) {
        this.walletAccountRepository = walletAccountRepository;
    }

    @PostMapping(value = "/populateAddressType", produces = "text/plain")
    public String populateAddressType() {
        LOG.info("Populating PRIVATE address type for all existing accounts");
        List<WalletAccount> migrated = this.walletAccountRepository.findAll().stream()
            .filter(walletAccount -> walletAccount.getType() == null)
            .map(walletAccount -> walletAccount.setType(WalletAccountType.PRIVATE))
            .collect(Collectors.toList());

        LOG.info("Updating {} accounts", migrated.size());
        walletAccountRepository.save(migrated);

        return String.format("Migrated %d accounts", migrated.size());
    }
}
