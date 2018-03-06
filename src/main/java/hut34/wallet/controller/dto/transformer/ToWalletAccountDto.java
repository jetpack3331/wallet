package hut34.wallet.controller.dto.transformer;

import hut34.wallet.controller.dto.WalletAccountDto;
import hut34.wallet.framework.BaseDto;
import hut34.wallet.model.WalletAccount;

import java.util.function.Function;

public class ToWalletAccountDto implements Function<WalletAccount, WalletAccountDto> {

    @Override
    public WalletAccountDto apply(WalletAccount walletAccount) {
        WalletAccountDto dto = BaseDto.fromEntity(new WalletAccountDto(), walletAccount);
        dto.setAddress(walletAccount.getAddress());
        dto.setSecretStorageJson(walletAccount.getSecretStorageJson());
        return dto;
    }

}
