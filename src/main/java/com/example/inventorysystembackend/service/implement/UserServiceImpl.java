package com.example.inventorysystembackend.service.implement;

import com.example.inventorysystembackend.dto.request.UserRequest;
import com.example.inventorysystembackend.dto.response.UserResponse;
import com.example.inventorysystembackend.exception.EmailAlreadyExistException;
import com.example.inventorysystembackend.exception.UserNotFoundException;
import com.example.inventorysystembackend.mapper.UserMapper;
import com.example.inventorysystembackend.model.entity.User;
import com.example.inventorysystembackend.repository.UserRepository;
import com.example.inventorysystembackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Creates a new user account.
     *
     * Process:
     * - maps the request DTO to a User entity
     * - encodes the raw password
     * - saves the user to the database
     *
     * @param request user registration data
     * @return saved user response
     */
    @Override
    public UserResponse createUser(UserRequest request) {

        log.info("[USER][CREATE] Start username={}", request.getUsername());

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        log.info("[USER][CREATE] Success id={} username={}", saved.getUserID(), saved.getUsername());

        return userMapper.toDTO(saved);


    }

    /**
     * Updates an existing user account.
     *
     * Process:
     * - retrieves the user by ID
     * - throws UserNotFoundException if the user does not exist
     * - updates editable user fields
     * - validates email uniqueness before updating
     * - encodes the password if a new password is provided
     * - saves the updated user
     * - returns the updated user as a response DTO
     *
     * @param userId ID of the user to update
     * @param request updated user data
     * @return updated user response
     */
    @Override
    public UserResponse updateUser(Long userId, UserRequest request) {

        log.info("[USER][UPDATE] Start id={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("[USER][UPDATE][NOT_FOUND] id={}", userId);
                    return new UserNotFoundException("User not found: " + userId);
                });

        user.setUsername(request.getUsername());

        if (!user.getEmail().equals(request.getEmail())) {

            boolean emailExists = userRepository.existsByEmail(request.getEmail());

            if (emailExists) {

                log.warn("[USER][UPDATE][EMAIL_EXISTS] email={}", request.getEmail());
                throw new EmailAlreadyExistException("Email already exists: " + request.getEmail());
            }

            user.setEmail(request.getEmail());

        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setRole(request.getRole());

        User updated = userRepository.save(user);

        log.info("[USER][UPDATE] Success id={}", userId);

        return userMapper.toDTO(updated);
    }

    /**
     * Delete user account
     *
     * Process:
     * - Validates if the user with that id exists
     * - Throw UserNotFoundException if the user does not exist
     * - Delete the user
     * @param userId id of the user to delete
     */
    @Override
    public void deleteUser(Long userId) {

        log.info("[USER][DELETE] Start id={}", userId);

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> {
                            log.warn("[USER][DELETE][NOT_FOUND] id={}", userId);
                            return new UserNotFoundException("User not found: " + userId);
                        });

        userRepository.delete(user);

        log.info("[USER][DELETE] SUCCESS id={}", userId);

    }

    /**
     * Get user account by id
     *
     * Process:
     * - Retrieve the user by id
     * - throws UserNotFoundException if the user does not exist
     * - return the user
     * @param userId id of the user to get
     * @return user response
     */
    @Override
    public UserResponse getUserById(Long userId) {

        log.info("[USER][GET] Start id={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("[USER][GET][NOT_FOUND] id={}", userId);
                    return new UserNotFoundException("User not found: " + userId);
                });

        log.info("[USER][GET] Success id={}", userId);

        return userMapper.toDTO(user);
    }

    /**
     * Get all users
     *
     * Process:
     * - Retrieve all the users
     *
     * @return user response
     */
    @Override
    public List<UserResponse> getAllUsers() {

        log.info("[USER][GET_ALL] Fetching all users");

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }


}
