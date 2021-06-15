package com.example.library.converter;

import com.example.library.domain.dto.UserDto;
import com.example.library.domain.entity.NameEmbeddable;
import com.example.library.domain.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto userDto) {
        User user = new User();
        NameEmbeddable nameEmbeddable = new NameEmbeddable();
        nameEmbeddable.setFirstName(userDto.getFirstName());
        nameEmbeddable.setMiddleName(userDto.getMiddleName());
        nameEmbeddable.setLastName(userDto.getLastName());
        user.setNameEmbeddable(nameEmbeddable);
        user.setEmail(userDto.getEmail());
        user.setUserType(userDto.getUserType());
        return user;
    }
}
