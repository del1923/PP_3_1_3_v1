package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserServiceImpl implements UserServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }


    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User showUser(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("Пользователь с таким ID не найден"));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public Set<User> getAllUsers() {
        return new LinkedHashSet<>(userRepository.findAll());
    }


    @Override
    @Transactional
    public void updateUser(User userUpdate, Long id) {
        userRepository.save(userUpdate);

        if (userRepository.findById(id).get().getPassword().equals(userUpdate.getPassword())) {
            userRepository.save(userRepository.findById(id).get());
        } else {
            userRepository.findById(id).get().setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            userRepository.save(userRepository.findById(id).get());
        }
        userRepository.save(userRepository.findById(id).get());
    }


    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("Пользователь с таким ID не найден"));
    }

    @Override
    @Transactional
    public void createUser(User user) {
        userRepository.save( user );
    }
}
