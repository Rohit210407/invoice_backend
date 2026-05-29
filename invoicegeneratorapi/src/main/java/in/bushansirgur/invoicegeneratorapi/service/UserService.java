package in.bushansirgur.invoicegeneratorapi.service;

import in.bushansirgur.invoicegeneratorapi.entity.User;
import in.bushansirgur.invoicegeneratorapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User saveOrUpdateUser(User user) {
        Optional<User> optionalUser = userRepository.findByClerkId(user.getClerkId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhotoUrl(user.getPhotoUrl());
            existingUser = userRepository.save(existingUser);
            return existingUser;
        }
        return userRepository.save(user);
    }

    @SuppressWarnings("null")
    public void deleteAccount(String clerkId) {
        User existingUser = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(existingUser);
    }

    public User getAccountByClerkId(String clerkId) {
        User existingUser = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return existingUser;
    }

    public User updateCompanyProfile(String clerkId, User profileUpdate) {
        User existingUser = userRepository.findByClerkId(clerkId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        existingUser.setCompanyName(profileUpdate.getCompanyName());
        existingUser.setCompanyEmail(profileUpdate.getCompanyEmail());
        existingUser.setCompanyPhone(profileUpdate.getCompanyPhone());
        existingUser.setCompanyAddress(profileUpdate.getCompanyAddress());
        existingUser.setCompanyGst(profileUpdate.getCompanyGst());
        existingUser.setHomeCurrency(profileUpdate.getHomeCurrency());
        return userRepository.save(existingUser);
    }
}
