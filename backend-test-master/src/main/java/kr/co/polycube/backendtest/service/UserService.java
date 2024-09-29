package kr.co.polycube.backendtest.service;

import kr.co.polycube.backendtest.model.User;
import kr.co.polycube.backendtest.repository.UserRepository;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    //@Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(String name) {
        User user = new User();
        user.setName(name);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            return user.get();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public User putUser(Long id, String name) {
        try {
            User user = new User(id, name);
            return userRepository.save(user);
        }
        catch (Exception e) {
            throw e;
        }
    }
}