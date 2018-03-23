package hut34.wallet.controller.dto.transformer;

import hut34.wallet.framework.usermanagement.dto.transformer.ToUserDto;

public interface Transformers {

    ToWalletAccountDto TO_WALLET_ACCOUNT_DTO = new ToWalletAccountDto();
    ToUserDto TO_USER_DTO = new ToUserDto();

}
