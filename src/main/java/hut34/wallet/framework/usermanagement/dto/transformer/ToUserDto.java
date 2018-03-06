package hut34.wallet.framework.usermanagement.dto.transformer;

import hut34.wallet.framework.BaseDto;
import hut34.wallet.framework.usermanagement.dto.UserDto;
import hut34.wallet.framework.usermanagement.model.User;

import java.util.function.Function;

public class ToUserDto implements Function<User, UserDto> {

    public static final ToUserDto INSTANCE = new ToUserDto();

    @Override
    public UserDto apply(User user) {
        UserDto dto = BaseDto.fromEntity(new UserDto(), user);
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        dto.setName(user.getName());
        dto.getRoles().addAll(user.getRoles());
        return dto;
    }
}
