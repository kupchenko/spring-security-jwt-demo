package me.kupchenko.service;

import me.kupchenko.dto.UserDto;
import me.kupchenko.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(UserDto userDto);

    List<User> getAll();

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void delete(Long id);
}
