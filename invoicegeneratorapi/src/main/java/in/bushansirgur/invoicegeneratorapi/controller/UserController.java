package in.bushansirgur.invoicegeneratorapi.controller;

import in.bushansirgur.invoicegeneratorapi.entity.User;
import in.bushansirgur.invoicegeneratorapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createOrUpdateUser(@RequestBody User user, Authentication authentication) {
        try {
            if (!authentication.getName().equals(user.getClerkId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "User does not have permission to access this resource");
            }
            return userService.saveOrUpdateUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/profile")
    public User getUserProfile(Authentication authentication) {
        try {
            return userService.getAccountByClerkId(authentication.getName());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile not found", e);
        }
    }

    @PutMapping("/profile")
    public User updateProfile(@RequestBody User profileUpdate, Authentication authentication) {
        try {
            return userService.updateCompanyProfile(authentication.getName(), profileUpdate);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update profile", e);
        }
    }
}
