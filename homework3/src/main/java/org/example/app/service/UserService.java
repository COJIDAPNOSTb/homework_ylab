package org.example.app.service;

import org.example.app.dto.UserDTO;
import org.example.app.mapper.UserMapper;
import org.example.app.model.User;
import org.example.app.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return null; // Или выбросить исключение, если пользователь не найден
        }
        return UserMapper.INSTANCE.toDTO(user);
    }

    public void createUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        userRepository.save(user);
    }

    public void updateUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        userRepository.update(user);
    }

    public void deleteUser(int id) {
        userRepository.delete(id);
    }
}
