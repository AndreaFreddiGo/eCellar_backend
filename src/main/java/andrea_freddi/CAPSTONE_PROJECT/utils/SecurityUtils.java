package andrea_freddi.CAPSTONE_PROJECT.utils;

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {

    /**
     * Checks if the user is the owner of the resource or an admin.
     * Throws BadRequestException if not authorized.
     *
     * @param user    the authenticated user
     * @param ownerId the UUID of the resource owner
     */

    // This method checks if the user is the owner of the resource or an admin
    public void checkOwnershipOrAdmin(User user, UUID ownerId) {
        if (!user.getId().equals(ownerId) && !user.isAdmin()) {
            throw new BadRequestException("You are not authorized to access this resource!");
        }
    }
}
