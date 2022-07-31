package com.poi.poi.service;

import com.poi.poi.exception.UserAlreadyExistException;
import com.poi.poi.model.User;
import com.poi.poi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private UserRepository userRepository;

    @Autowired
    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(User user) throws UserAlreadyExistException {
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("Username already exists");
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

}
