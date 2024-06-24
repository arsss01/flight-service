package air_astana.flight_service.config.security;

import air_astana.flight_service.exceptions.GlobalException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@UtilityClass
public class AuditorUtils {
    public static Long getCurrentUserId() {
        String id;
        try {
            id = getCurrentUser().getPassword();
        } catch (Exception e) {
            throw new GlobalException("Cannot get current userId from Authentication!!!");
        }
        return Long.parseLong(id);
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
