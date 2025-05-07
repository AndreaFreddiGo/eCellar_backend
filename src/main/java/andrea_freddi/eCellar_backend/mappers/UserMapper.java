package andrea_freddi.eCellar_backend.mappers;

import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.payloads.UserDTO;
import org.springframework.stereotype.Component;

// This class is responsible for mapping between User entities and UserDTOs

@Component
public class UserMapper {

    // Converts a User entity to a UserDTO
    public UserDTO userToDTO(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getUsername(),
                user.getBiography(),
                user.getPhone(),
                user.getBirthDate(),
                user.getProfilePicture(),
                user.getPreferredLanguage(),
                user.isPublicProfile(),
                user.getRegistrationDate()
        );
    }
}
