package bg.softuni.pathfinder.service.session;

import bg.softuni.pathfinder.model.Role;
import bg.softuni.pathfinder.model.enums.UserRoles;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class LoggedUser {

    private String username;
    private Set<Role> roles;
    private boolean isLogged;

    public LoggedUser () {

        this.roles = new HashSet<>();
    }

    public void reset() {
        this
                .setUsername(null)
                .setRoles(Collections.emptySet())
                .setLogged(false);
    }

    public LoggedUser setUsername (String username) {

        this.username = username;
        return this;
    }


    public LoggedUser setRoles (Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public boolean isLogged () {
        return isLogged;
    }

    public LoggedUser setLogged (boolean logged) {
        isLogged = logged;
        return this;
    }

    public boolean isAdmin () {
        return hasRole(UserRoles.ADMIN);
    }
    public boolean isModerator () {
        return hasRole(UserRoles.MODERATOR);
    }
    public boolean isOnlyUser () {
        return this.roles.stream()
                .allMatch(role -> role.getName().equals(UserRoles.USER));
    }

    private boolean hasRole (UserRoles userRoles) {
        return this.roles.stream()
                .anyMatch(role -> role.getName().equals(userRoles));
    }
}
