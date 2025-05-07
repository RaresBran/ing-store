package com.ing.store.service;

import com.ing.store.dto.UserDto;
import com.ing.store.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final ModelMapper mapper;

    public UserDto getCurrentUserDto() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mapper.map(user, UserDto.class);
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
