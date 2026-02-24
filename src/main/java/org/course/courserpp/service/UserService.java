package org.course.courserpp.service;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.Role;
import org.course.courserpp.model.User;
import org.course.courserpp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + login));
    }

    public void register(String login, String password, Role role) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать не менее 6 символов");
        }
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + id));
    }
}
