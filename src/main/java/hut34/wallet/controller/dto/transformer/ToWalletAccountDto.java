package hut34.wallet.controller.dto.transformer;

import hut34.wallet.controller.dto.WalletAccountDto;
import hut34.wallet.framework.BaseDto;
import hut34.wallet.framework.Transformer;
import hut34.wallet.model.WalletAccount;

public class ToWalletAccountDto implements Transformer<WalletAccount, WalletAccountDto> {

    @Override
    public WalletAccountDto apply(WalletAccount walletAccount) {
        WalletAccountDto dto = BaseDto.fromEntity(new WalletAccountDto(), walletAccount);
        dto.setAddress(walletAccount.getAddress());
        dto.setSecretStorageJson(walletAccount.getSecretStorageJson());
        return dto;
    }

}
