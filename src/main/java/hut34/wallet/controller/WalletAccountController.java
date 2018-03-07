package hut34.wallet.controller;

import hut34.wallet.controller.dto.CreateWalletRequest;
import hut34.wallet.controller.dto.WalletAccountDto;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.service.WalletAccountService;
import hut34.wallet.util.NotFoundException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

import static hut34.wallet.controller.dto.transformer.Transformers.TO_WALLET_ACCOUNT_DTO;

@RestController
public class WalletAccountController {

    private final WalletAccountService walletAccountService;

    public WalletAccountController(WalletAccountService walletAccountService) {
        this.walletAccountService = walletAccountService;
    }

    @PostMapping("/api/wallets/accounts")
    public WalletAccountDto create(@RequestBody CreateWalletRequest request) {
        WalletAccount walletAccount = walletAccountService.create(request.getAddress(), request.getSecretStorageJson());
        return TO_WALLET_ACCOUNT_DTO.apply(walletAccount);
    }

    @GetMapping("/api/wallets/accounts/mine")
    public List<WalletAccountDto> listForCurrentUser() {
        return TO_WALLET_ACCOUNT_DTO
            .transform(walletAccountService.listForCurrentUser());
    }

    @GetMapping("/api/wallets/accounts/{address}/download")
    public void download(@PathVariable("address") String address, HttpServletResponse response) throws IOException {
        Optional<WalletAccount> optional = walletAccountService.get(address);
        WalletAccount walletAccount = optional.orElseThrow(NotFoundException::new);

        String filename = generateKeystoreFilename(walletAccount);
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s;", filename));
        response.setContentType("application/json");

        byte[] jsonBytes = walletAccount.getSecretStorageJson().getBytes(StandardCharsets.UTF_8);
        FileCopyUtils.copy(jsonBytes, response.getOutputStream());
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
