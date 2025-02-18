package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import com.vladkostrov.usermicroserviceapp.enums.UserStatus;
import com.vladkostrov.usermicroserviceapp.exception.UserNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        user.setStatus(UserStatus.ACTIVE);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        return userRepository.save(user);
    }
    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found with id: " + id));
    }

    public List<UserEntity> getAllActiveUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getStatus().equals(UserStatus.ACTIVE)).collect(Collectors.toList());
    }

    public List<UserEntity> getAllBannedUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getStatus().equals(UserStatus.BANNED)).collect(Collectors.toList());
    }

    public List<UserEntity> getAllDeletedUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getStatus().equals(UserStatus.DELETED)).collect(Collectors.toList());
    }

    public UserEntity updateUser(UserEntity user) {
        if(!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("No user found to update with id: " + user.getId());
        }
        return userRepository.save(user);
    }
    public void softDeleteUserById(UUID id) {
        UserEntity userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user found to soft delete with id: " + id));
        userToDelete.setStatus(UserStatus.DELETED);
        userRepository.save(userToDelete);
    }

    public void hardDeleteUserById(UUID id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException("No user found to hard delete with id: " + id);
        }
        userRepository.deleteById(id);
    }

}
