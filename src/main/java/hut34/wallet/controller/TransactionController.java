package hut34.wallet.controller;

import hut34.wallet.client.gas.GasClient;
import hut34.wallet.client.gas.GasInfo;
import hut34.wallet.client.transact.TransactionClient;
import hut34.wallet.controller.dto.SimpleRequest;
import hut34.wallet.controller.dto.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private final TransactionClient transactionClient;
    private final GasClient gasClient;

    public TransactionController(TransactionClient transactionClient, GasClient gasClient) {
        this.transactionClient = transactionClient;
        this.gasClient = gasClient;
    }

    @PostMapping("/api/transactions")
    public SimpleResponse<String> submitSignedTransaction(@RequestBody SimpleRequest<String> transaction) {
        LOG.info("Sending signed transaction");
        String transactionHash = transactionClient.sendSignedTransaction(transaction.getValue());
        LOG.info("Sent transaction with hash {}", transactionHash);
        return new SimpleResponse<>(transactionHash);
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
