package hut34.wallet.controller;

import hut34.wallet.controller.dto.CreateWalletRequest;
import hut34.wallet.controller.dto.WalletAccountDto;
import hut34.wallet.controller.dto.transformer.Transformers;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.service.WalletAccountService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class WalletAccountController {

    private final WalletAccountService walletAccountService;

    public WalletAccountController(WalletAccountService walletAccountService) {
        this.walletAccountService = walletAccountService;
    }

    @RequestMapping(method = POST, path = "/api/wallets/accounts")
    public WalletAccountDto create(@RequestBody CreateWalletRequest request) {
        WalletAccount walletAccount = walletAccountService.create(request.getAddress(), request.getEncryptedPrivateKey());
        return Transformers.TO_WALLET_ACCOUNT_DTO.apply(walletAccount);
    }

}
