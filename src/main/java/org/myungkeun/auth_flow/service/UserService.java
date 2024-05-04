package org.myungkeun.auth_flow.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.myungkeun.auth_flow.Dto.UserCreateRequestDto;
import org.myungkeun.auth_flow.Exception.AccountAlreadyExistsException;
import org.myungkeun.auth_flow.entity.User;
import org.myungkeun.auth_flow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void create(@NonNull final UserCreateRequestDto request) {
        final var emailId = request.getEmailId();
        final var userAccountExistsWithEmailId = userRepository.existsByEmailId(emailId);

        if (Boolean.TRUE.equals(userAccountExistsWithEmailId)) {
            throw new AccountAlreadyExistsException("Account with provided email-id already exists");
        }

        final var encodedPassword = passwordEncoder.encode(request.getPassword());

        final var user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encodedPassword);
        user.setEmailId(request.getEmailId());

        userRepository.save(user);
    }


}
