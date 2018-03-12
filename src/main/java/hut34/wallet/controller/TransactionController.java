package hut34.wallet.controller;

import hut34.wallet.client.gas.GasClient;
import hut34.wallet.client.gas.GasInfo;
import hut34.wallet.client.transact.TransactionClient;
import hut34.wallet.controller.dto.SimpleRequest;
import hut34.wallet.controller.dto.SimpleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionClient transactionClient;
    private final GasClient gasClient;

    public TransactionController(TransactionClient transactionClient, GasClient gasClient) {
        this.transactionClient = transactionClient;
        this.gasClient = gasClient;
    }

    @PostMapping("/api/transactions")
    public SimpleResponse<String> submitSignedTransaction(@RequestBody SimpleRequest<String> transaction) {
        String transactionHash = transactionClient.sendSignedTransaction(transaction.getValue());
        return new SimpleResponse<>(transactionHash);
    }

    @GetMapping("/api/gas")
    public GasInfo getGasInfo() {
        return gasClient.getGasInfo();
    }

}
