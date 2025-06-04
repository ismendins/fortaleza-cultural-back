package fortcultural.arquitetura.service.impl;

import fortcultural.arquitetura.model.entity.User;
import fortcultural.arquitetura.repository.UserRepository;
import fortcultural.arquitetura.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setType(updatedUser.getType());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User com ID " +  id + " não encontrado."));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
