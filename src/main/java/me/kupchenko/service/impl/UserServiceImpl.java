package me.kupchenko.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kupchenko.dto.UserDto;
import me.kupchenko.exception.RoleNotFoundException;
import me.kupchenko.mapper.UserMapper;
import me.kupchenko.model.Role;
import me.kupchenko.model.Status;
import me.kupchenko.model.User;
import me.kupchenko.repository.RoleRepository;
import me.kupchenko.repository.UserRepository;
import me.kupchenko.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        Role roleUser = roleRepository.findByName(ROLE_USER).orElseThrow(RoleNotFoundException::new);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleUser));
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("User: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("{} users found", result.size());
        return result;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        result.ifPresent(user -> {
            log.info("User: {} found by username: {}", result.get(), username);
        });
        return result;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.info("User with id: {} successfully deleted", id);
        userRepository.deleteById(id);
    }
}
