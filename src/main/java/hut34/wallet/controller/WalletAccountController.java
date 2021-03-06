package hut34.wallet.controller;

import hut34.wallet.client.etherscan.EtherscanClient;
import hut34.wallet.client.etherscan.Sort;
import hut34.wallet.controller.dto.CreateWalletRequest;
import hut34.wallet.controller.dto.SimpleResponse;
import hut34.wallet.controller.dto.WalletAccountBalance;
import hut34.wallet.controller.dto.WalletAccountDto;
import hut34.wallet.controller.dto.WalletAccountTokenBalance;
import hut34.wallet.controller.dto.WalletAccountTransactions;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.model.WalletAccountType;
import hut34.wallet.service.ManagedAccountService;
import hut34.wallet.service.WalletAccountService;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Numeric;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import static hut34.wallet.controller.dto.transformer.Transformers.TO_WALLET_ACCOUNT_DTO;

@RestController
public class WalletAccountController {

    private final WalletAccountService walletAccountService;
    private final EtherscanClient etherscanClient;
    private final ManagedAccountService managedAccountService;

    public WalletAccountController(WalletAccountService walletAccountService, EtherscanClient etherscanClient, ManagedAccountService managedAccountService) {
        this.walletAccountService = walletAccountService;
        this.etherscanClient = etherscanClient;
        this.managedAccountService = managedAccountService;
    }

    @PostMapping("/api/wallets/accounts")
    public WalletAccountDto create(@Valid @RequestBody CreateWalletRequest request) {

        if (request.getType() == WalletAccountType.PRIVATE) {
            WalletAccount walletAccount = walletAccountService.createPrivate(request.getAddress(), request.getSecretStorageJson());
            return TO_WALLET_ACCOUNT_DTO.apply(walletAccount);
        }

        if (request.getType() == WalletAccountType.MANAGED) {
            WalletAccount walletAccount = walletAccountService.createManaged();
            return TO_WALLET_ACCOUNT_DTO.apply(walletAccount);
        }

        throw new IllegalArgumentException("Unsupported wallet type");
    }

    @GetMapping("/api/wallets/accounts/mine")
    public List<WalletAccountDto> listForCurrentUser() {
        return TO_WALLET_ACCOUNT_DTO
            .transform(walletAccountService.listForCurrentUser());
    }

    @GetMapping("/api/wallets/accounts/{address}/balance")
    public WalletAccountBalance getBalance(@PathVariable String address) {
        return new WalletAccountBalance(address, etherscanClient.getBalance(address));
    }

    @GetMapping("/api/wallets/accounts/{address}/tokens/{contractAddress}/balance")
    public WalletAccountTokenBalance getTokenBalance(@PathVariable String contractAddress, @PathVariable String address) {
        return new WalletAccountTokenBalance(contractAddress, address, etherscanClient.getTokenBalance(contractAddress, address));
    }

    @GetMapping("/api/wallets/accounts/{address}/transactions")
    public WalletAccountTransactions getTransactions(@PathVariable String address) {
        return new WalletAccountTransactions(address, etherscanClient.getTransactions(address, Sort.DESC));
    }

    @GetMapping("/api/wallets/accounts/{address}/download")
    public void download(@PathVariable("address") String address, HttpServletResponse response) throws IOException {
        WalletAccount walletAccount = walletAccountService.getOrThrow(address);

        String filename = generateKeystoreFilename(walletAccount);
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s;", filename));
        response.setContentType("application/json");

        byte[] jsonBytes = walletAccount.getSecretStorageJson().getBytes(StandardCharsets.UTF_8);
        FileCopyUtils.copy(jsonBytes, response.getOutputStream());
    }

    @GetMapping("/api/wallets/accounts/{address}/privateKey")
    public SimpleResponse<String> getPrivateKey(@PathVariable String address) {
        WalletAccount walletAccount = walletAccountService.getOrThrow(address);

        Credentials credentials = managedAccountService.loadCredentials(walletAccount);
        String hexKey = Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey());
        return new SimpleResponse<>(hexKey);
    }

    private String generateKeystoreFilename(WalletAccount walletAccount) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendLiteral("UTC--")
            .appendInstant(3)
            .appendLiteral("--")
            .appendLiteral(walletAccount.getAddress())
            .toFormatter();
        return formatter.format(walletAccount.getCreated()).replace(":", "-");
    }
}
