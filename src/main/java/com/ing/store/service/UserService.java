package com.ing.store.service;

import com.ing.store.dto.UserDto;
import com.ing.store.model.entity.User;
import com.ing.store.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserDto getCurrentUser(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(EntityNotFoundException::new);
        return mapper.map(user, UserDto.class);
    }
}
