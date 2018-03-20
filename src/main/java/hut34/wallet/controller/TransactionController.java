package hut34.wallet.controller;

import hut34.wallet.client.gas.GasClient;
import hut34.wallet.client.gas.GasInfo;
import hut34.wallet.client.transact.TransactionClient;
import hut34.wallet.controller.dto.CreateTransactionRequest;
import hut34.wallet.controller.dto.SimpleRequest;
import hut34.wallet.controller.dto.SimpleResponse;
import hut34.wallet.model.WalletAccount;
import hut34.wallet.service.ManagedAccountService;
import hut34.wallet.service.WalletAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import javax.validation.Valid;
import java.math.BigInteger;

@RestController
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private final TransactionClient transactionClient;
    private final GasClient gasClient;
    private final ManagedAccountService managedAccountService;
    private final WalletAccountService walletAccountService;

    public TransactionController(TransactionClient transactionClient,
                                 GasClient gasClient,
                                 ManagedAccountService managedAccountService,
                                 WalletAccountService walletAccountService) {
        this.transactionClient = transactionClient;
        this.gasClient = gasClient;
        this.managedAccountService = managedAccountService;
        this.walletAccountService = walletAccountService;
    }

    @PostMapping("/api/transactions")
    public SimpleResponse<String> submitSignedTransaction(@RequestBody SimpleRequest<String> transaction) {
        LOG.info("Sending signed transaction");
        String transactionHash = transactionClient.sendSignedTransaction(transaction.getValue());
        LOG.info("Sent transaction with hash {}", transactionHash);
        return new SimpleResponse<>(transactionHash);
    }

    @PostMapping("/api/transactions/sign")
    public SimpleResponse<String> signTransaction(@Valid @RequestBody CreateTransactionRequest txnRequest) {
        LOG.info("Creating signed transaction");

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
            new BigInteger(txnRequest.getNonce()),
            new BigInteger(txnRequest.getGasPrice()),
            new BigInteger(txnRequest.getGasLimit()),
            txnRequest.getTo(),
            new BigInteger(txnRequest.getValue()));

        WalletAccount walletAccount = walletAccountService.getOrThrow(txnRequest.getFrom());
        Credentials credentials = managedAccountService.loadCredentials(walletAccount);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        LOG.info("Transaction signed successfully");
        return new SimpleResponse<>(hexValue);
    }

    @GetMapping("/api/accounts/{address}/nonce")
    public SimpleResponse<BigInteger> getNextNonce(@PathVariable String address) {
        BigInteger nextNonce = transactionClient.getNextNonce(address);
        return new SimpleResponse<>(nextNonce);
    }

    @GetMapping("/api/gas")
    public GasInfo getGasInfo() {
        return gasClient.getGasInfo();
    }

}
