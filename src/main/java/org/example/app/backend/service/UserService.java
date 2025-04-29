package org.example.app.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.app.backend.model.User;
import org.example.app.backend.model.dto.UserCreateDto;
import org.example.app.backend.model.dto.UserDto;
import org.example.app.backend.model.dto.UserMapper;
import org.example.app.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService{

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    // Для шифрования паролей, вам нужно добавить это
    private final PasswordEncoder passwordEncoder;

    public Page<UserDto> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toUserDto);
    }

    public UserDto getOne(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userMapper.toUserDto(userOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<UserDto> getMany(List<UUID> ids) {
        List<User> users = userRepository.findAllById(ids);
        return users.stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    // Изменяем метод create для работы с UserCreateDto
    public UserDto create(UserCreateDto createDto) {
        User user = userMapper.toEntity(createDto);

        // Шифруем пароль перед сохранением
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User resultUser = userRepository.save(user);
        return userMapper.toUserDto(resultUser);
    }

    // Оставляем старый метод для обратной совместимости, но помечаем его устаревшим
    @Deprecated
    public UserDto create(@Nullable UserDto dto) {
        User user = userMapper.toEntity(dto);
        User resultUser = userRepository.save(user);
        return userMapper.toUserDto(resultUser);
    }

    public UserDto patch(UUID id, JsonNode patchNode) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        UserDto userDto = userMapper.toUserDto(user);
        objectMapper.readerForUpdating(userDto).readValue(patchNode);
        userMapper.updateWithNull(userDto, user);

        User resultUser = userRepository.save(user);
        return userMapper.toUserDto(resultUser);
    }

    public List<UUID> patchMany(List<UUID> ids, JsonNode patchNode) throws IOException {
        Collection<User> users = userRepository.findAllById(ids);

        for (User user : users) {
            UserDto userDto = userMapper.toUserDto(user);
            objectMapper.readerForUpdating(userDto).readValue(patchNode);
            userMapper.updateWithNull(userDto, user);
        }

        List<User> resultUsers = userRepository.saveAll(users);
        return resultUsers.stream()
                .map(User::getId)
                .toList();
    }

    public UserDto delete(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        return userMapper.toUserDto(user);
    }

    public void deleteMany(List<UUID> ids) {
        userRepository.deleteAllById(ids);
    }
}
