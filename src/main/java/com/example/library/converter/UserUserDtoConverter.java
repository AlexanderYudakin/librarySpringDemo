package com.example.library.converter;

import com.example.library.domain.dto.UserDto;
import com.example.library.domain.entity.NameEmbeddable;
import com.example.library.domain.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUserDtoConverter implements Converter<User, UserDto>  {
    @Override
    public UserDto convert(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUserType(user.getUserType());
        //userDto.setFirstName(Optional.ofNullable(user.getNameEmbeddable()).map(NameEmbeddable::getFirstName).orElse(null));
        if (user.getNameEmbeddable() != null) {
            userDto.setFirstName(user.getNameEmbeddable().getFirstName());
            userDto.setLastName(user.getNameEmbeddable().getLastName());
            userDto.setMiddleName(user.getNameEmbeddable().getMiddleName());
        }
        return userDto;
    }
}
