package bg.softuni.pathfinder.service.impl;

import bg.softuni.pathfinder.model.User;
import bg.softuni.pathfinder.repository.UserRepository;
import bg.softuni.pathfinder.service.UserService;
import bg.softuni.pathfinder.service.session.LoggedUser;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoggedUser loggedUser;

    public UserServiceImpl(UserRepository userRepository, LoggedUser loggedUser) {
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
    }


    @Override
    public User getLoggedUser() {
        return userRepository.findByUsername(loggedUser.getUsername());
    }
}
