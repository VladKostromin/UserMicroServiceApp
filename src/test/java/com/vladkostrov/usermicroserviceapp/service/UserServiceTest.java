package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import com.vladkostrov.usermicroserviceapp.enums.UserStatus;
import com.vladkostrov.usermicroserviceapp.exception.UserNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.UserRepository;
import com.vladkostrov.usermicroserviceapp.utils.UserDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceUnderTest;

    @Test
    @DisplayName("Test create user success functionality")
    public void givenUserEntity_whenCreateUser_thenUserIsCreated() {
        //given
        UserEntity userEntity = UserDataUtils.getUserTransient();
        BDDMockito.given(userRepository.save(userEntity)).willReturn(UserDataUtils.getUserPersistent());
        //when
        UserEntity createdUser = userServiceUnderTest.createUser(userEntity);
        //then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("Test getUserById success functionality")
    public void givenUserId_whenGetUserById_thenUserIsRetrieved() {
        //given
        UUID userId = UserDataUtils.getUserPersistent().getId();
        BDDMockito.given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(UserDataUtils.getUserPersistent()));
        //when
        UserEntity retrievedUser = userServiceUnderTest.getUserById(userId);
        //then
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getId()).isEqualTo(userId);
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Test getUserById fail functionality")
    public void givenUserId_whenGetUserById_thenUserNotFoundExceptionThrown() {
        //given
        UUID userId = UserDataUtils.getUserPersistent().getId();
        BDDMockito.given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userServiceUnderTest.getUserById(userId));
        //then
        assertThat(userNotFoundException.getMessage()).isEqualTo("No user found with id: " + userId);
        assertThat(userNotFoundException.getErrorCode()).isEqualTo("USER_NOT_FOUND_ERROR_CODE");
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Test update user success functionality")
    public void givenUserEntity_whenUpdateUser_thenUserIsUpdated() {
        //given
        UserEntity userToUpdate = UserDataUtils.getUserPersistent();
        userToUpdate.setEmail("testUpdate@email.com");
        BDDMockito.given(userRepository.existsById(any(UUID.class))).willReturn(true);
        BDDMockito.given(userRepository.save(any(UserEntity.class))).willReturn(userToUpdate);
        //when
        UserEntity updatedUser = userServiceUnderTest.updateUser(userToUpdate);
        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isNotEqualTo(UserDataUtils.getUserPersistent().getEmail());
        verify(userRepository, times(1)).existsById(any(UUID.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
    @Test
    @DisplayName("Test update user fail functionality")
    public void givenUserEntity_whenUpdateUser_thenUserNotFoundExceptionIsThrown() {
        //given
        UserEntity userToUpdate = UserDataUtils.getUserPersistent();
        BDDMockito.given(userRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userServiceUnderTest.updateUser(userToUpdate));
        //then
        assertThat(userNotFoundException.getMessage()).isEqualTo("No user found to update with id: " + userToUpdate.getId());
        assertThat(userNotFoundException.getErrorCode()).isEqualTo("USER_NOT_FOUND_ERROR_CODE");
        verify(userRepository, times(1)).existsById(any(UUID.class));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Test get all active users functionality")
    public void givenListOfUsers_whenGetAllUsers_thenActiveUsersIsRetrieved() {
        //given
        UserEntity user1 = UserDataUtils.getUserPersistent();
        UserEntity user2 = UserDataUtils.getUserPersistent();
        UserEntity user3 = UserDataUtils.getUserPersistent();
        user3.setStatus(UserStatus.DELETED);
        UserEntity user4 = UserDataUtils.getUserPersistent();
        user4.setStatus(UserStatus.BANNED);
        List<UserEntity> users = List.of(user1, user2, user3, user4);
        BDDMockito.given(userRepository.findAll()).willReturn(users);
        //when
        List<UserEntity> retrievedUsers = userServiceUnderTest.getAllActiveUsers();
        //then
        assertThat(CollectionUtils.isEmpty(retrievedUsers)).isFalse();
        assertThat(retrievedUsers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test get all banned users functionality")
    public void givenListOfUsers_whenGetAllUsers_thenBannedUsersIsRetrieved() {
        //given
        UserEntity user1 = UserDataUtils.getUserPersistent();
        user1.setStatus(UserStatus.BANNED);
        UserEntity user2 = UserDataUtils.getUserPersistent();
        user2.setStatus(UserStatus.ACTIVE);
        UserEntity user3 = UserDataUtils.getUserPersistent();
        user3.setStatus(UserStatus.DELETED);
        UserEntity user4 = UserDataUtils.getUserPersistent();
        user4.setStatus(UserStatus.ACTIVE);
        List<UserEntity> users = List.of(user1, user2, user3);
        BDDMockito.given(userRepository.findAll()).willReturn(users);
        //when
        List<UserEntity> retrievedUsers = userServiceUnderTest.getAllBannedUsers();
        //then
        assertThat(CollectionUtils.isEmpty(retrievedUsers)).isFalse();
        assertThat(retrievedUsers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Test get all deleted users functionality")
    public void givenListOfUsers_whenGetAllUsers_thenDeletedUsersIsRetrieved() {
        //given
        UserEntity user1 = UserDataUtils.getUserPersistent();
        user1.setStatus(UserStatus.DELETED);
        UserEntity user2 = UserDataUtils.getUserPersistent();
        user2.setStatus(UserStatus.DELETED);
        UserEntity user3 = UserDataUtils.getUserPersistent();
        user3.setStatus(UserStatus.DELETED);
        UserEntity user4 = UserDataUtils.getUserPersistent();
        user4.setStatus(UserStatus.ACTIVE);
        List<UserEntity> users = List.of(user1, user2, user3);
        BDDMockito.given(userRepository.findAll()).willReturn(users);
        //when
        List<UserEntity> retrievedUsers = userServiceUnderTest.getAllDeletedUsers();
        //then
        assertThat(CollectionUtils.isEmpty(retrievedUsers)).isFalse();
        assertThat(retrievedUsers.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test soft delete user success functionality")
    public void givenUserId_whenSoftDeleteUserById_thenUserStatusIsDeleted() {
        //given
        UserEntity userToDelete = UserDataUtils.getUserPersistent();
        BDDMockito.given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(userToDelete));
        //when
        userServiceUnderTest.softDeleteUserById(UserDataUtils.getUserPersistent().getId());
        //then
        assertThat(userToDelete.getStatus()).isEqualTo(UserStatus.DELETED);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userRepository, never()).deleteById(any(UUID.class));
    }
    @Test
    @DisplayName("Test soft delete user fail functionality")
    public void givenUserId_whenSoftDeleteUserById_thenUserNotFoundExceptionIsThrown() {
        //given
        UUID userToDeleteId = UserDataUtils.getUserPersistent().getId();
        BDDMockito.given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userServiceUnderTest.softDeleteUserById(userToDeleteId));
        //then
        assertThat(userNotFoundException.getMessage()).isEqualTo("No user found to soft delete with id: " + userToDeleteId);
        assertThat(userNotFoundException.getErrorCode()).isEqualTo("USER_NOT_FOUND_ERROR_CODE");
        verify(userRepository, never()).save(any(UserEntity.class));

    }

    @Test
    @DisplayName("Test hard delete user success functionality")
    public void givenUserId_whenHardDeleteUserById_thenUserIsDeleted() {
        //given
        BDDMockito.given(userRepository.existsById(any(UUID.class))).willReturn(true);
        //when
        userServiceUnderTest.hardDeleteUserById(UserDataUtils.getUserPersistent().getId());
        //then
        verify(userRepository, times(1)).deleteById(any(UUID.class));
    }
    @Test
    @DisplayName("Test hard delete user fail functionality")
    public void givenUserId_whenHardDeleteUserById_thenUserNotFoundExceptionIsThrown() {
        //given
        UUID userToDeleteId = UserDataUtils.getUserPersistent().getId();
        BDDMockito.given(userRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userServiceUnderTest.hardDeleteUserById(userToDeleteId));
        //then
        assertThat(userNotFoundException.getMessage()).isEqualTo("No user found to hard delete with id: " + userToDeleteId);
        assertThat(userNotFoundException.getErrorCode()).isEqualTo("USER_NOT_FOUND_ERROR_CODE");
        verify(userRepository, never()).deleteById(any(UUID.class));

    }

}
