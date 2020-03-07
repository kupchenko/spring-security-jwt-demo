package me.kupchenko.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kupchenko.model.Role;
import me.kupchenko.model.Status;
import me.kupchenko.model.User;
import me.kupchenko.security.jwt.JwtUserDetails;
import me.kupchenko.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

	private final UserService userService;

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
		return userRoles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));

		JwtUserDetails jwtUser = JwtUserDetails.builder()
				.id(user.getId())
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.email(user.getEmail())
				.password(user.getPassword())
				.enabled(user.getStatus().equals(Status.ACTIVE))
				.authorities(mapToGrantedAuthorities(new ArrayList<>(user.getRoles())))
				.build();
		log.info("User with username: {} successfully loaded", username);
		return jwtUser;
	}
}
