package air_astana.flight_service.config.security;

import air_astana.flight_service.exceptions.GlobalException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@UtilityClass
public class AuditorUtils {
    private final static String ADMIN = "ADMIN";

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

    public static boolean hasAdminRole() {
        User user = getCurrentUser();
        if (user == null || user.getAuthorities() == null) {
            return false;
        }
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (ADMIN.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
