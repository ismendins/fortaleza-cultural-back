package fortcultural.arquitetura.service.interfaces;

import fortcultural.arquitetura.model.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    List<User> listUsers();
    Optional<User> findUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}