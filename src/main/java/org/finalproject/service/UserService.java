package org.finalproject.service;

import org.finalproject.entity.User;
import org.finalproject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // REGISTER
    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email đã tồn tại";
        }

        user.setPassword(encoder.encode(user.getPassword())); //HASH
        user.setRole("CUSTOMER");

        userRepository.save(user);
        return "SUCCESS";
    }

    // LOGIN
    public User login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() &&
                encoder.matches(password, user.get().getPassword())) {
            return user.get();
        }

        return null;
    }

    // UPDATE PROFILE
    public void updateProfile(User formUser) {

        User dbUser = userRepository.findById(formUser.getId())
                .orElseThrow();

        //chỉ update field cho phép
        dbUser.setName(formUser.getName());
        dbUser.setPhone(formUser.getPhone());
        dbUser.setAddress(formUser.getAddress());

        userRepository.save(dbUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
