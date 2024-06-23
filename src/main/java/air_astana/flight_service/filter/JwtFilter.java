package air_astana.flight_service.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String jwt = getTokenFromHttpRequest(httpServletRequest);
            if (jwt != null) {
                Authentication authentication = this.getAuthentication(jwt);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
            throw e;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromHttpRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * Parse jwt token and return {@link Authentication}
     *
     * @param token token encoded in base64
     * @return {@link Authentication} or null if token is not valid
     */
    private Authentication getAuthentication(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Claim> claims = jwt.getClaims();

            Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
            if (claims.get("role") != null) {
                authorities = List.of(new SimpleGrantedAuthority(claims.get("role").asString()));
            }

            String userIdInsteadOfPassword = "";
            if (claims.get("userId") != null) {
                userIdInsteadOfPassword = String.valueOf(claims.get("userId"));
            }

            User principal = new User(claims.get("sub").asString(), userIdInsteadOfPassword, authorities);

            return new PreAuthenticatedAuthenticationToken(principal, token, authorities);
        } catch (Exception e) {
            log.error("Authentication error : ", e);
            return null;
        }
    }
}

