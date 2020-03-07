package me.kupchenko.controller;

import lombok.RequiredArgsConstructor;
import me.kupchenko.dto.AdminUserDto;
import me.kupchenko.mapper.UserMapper;
import me.kupchenko.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    private final UserService userService;

    @Secured("hasRole('USER') or hasPermission('')")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable(name = "id") Long id) {
        return userService.findById(id).map(user -> {
            AdminUserDto result = UserMapper.userToAdminUserDto(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
