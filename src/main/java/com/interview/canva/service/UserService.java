package com.interview.canva.service;

import com.interview.canva.dto.CreateUserRequest;
import com.interview.canva.dto.GetUserResponse;
import com.interview.canva.entity.User;
import com.interview.canva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<GetUserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToGetUserResponse)
                .toList();
    }

    public Optional<GetUserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToGetUserResponse);
    }

    public GetUserResponse createUser(CreateUserRequest request) {
        User user = mapToUser(request);
        User savedUser = userRepository.save(user);
        return mapToGetUserResponse(savedUser);
    }

    public Optional<GetUserResponse> updateUser(Long id, CreateUserRequest request) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(request.firstName());
                    existingUser.setLastName(request.lastName());
                    User updatedUser = userRepository.save(existingUser);
                    return mapToGetUserResponse(updatedUser);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Private mapping methods
    private User mapToUser(CreateUserRequest request) {
        return new User(
            request.firstName(),
            request.lastName(),
            request.email()
        );
    }

    private GetUserResponse mapToGetUserResponse(User user) {
        return new GetUserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
    }
}
