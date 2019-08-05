package me.kupchenko.rest;

import lombok.RequiredArgsConstructor;
import me.kupchenko.dto.UserDto;
import me.kupchenko.mapper.UserMapper;
import me.kupchenko.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        return userService.findById(id).map(user -> {
            UserDto result = UserMapper.userToUserDto(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
