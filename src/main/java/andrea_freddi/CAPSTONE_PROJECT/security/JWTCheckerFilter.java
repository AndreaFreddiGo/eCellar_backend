package andrea_freddi.CAPSTONE_PROJECT.security;

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.UnauthorizedException;
import andrea_freddi.CAPSTONE_PROJECT.services.UsersService;
import andrea_freddi.CAPSTONE_PROJECT.tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// This class is a filter that checks the validity of the JWT token in the Authorization header of incoming requests

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    // @Autowired annotation is used to inject the JWT and UsersService beans into this class
    @Autowired
    private JWT jwt;
    @Autowired
    private UsersService usersService;

    // This method is called for every incoming request
    // It checks the Authorization header for a valid JWT token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        String accessToken = authHeader.substring(7);

        jwt.verifyToken(accessToken);

        // If the token is valid, it extracts the user ID from the token and retrieve the user from the database
        String userId = jwt.getIdFromToken(accessToken);
        User currentUser = this.usersService.findById(UUID.fromString(userId));

        // Once the user is retrieved, it creates an Authentication object and sets it in the SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); // Aggiorniamo il SecurityContext associandogli l'utente autenticato

        filterChain.doFilter(request, response);
    }

    // This method excludes the filter from being applied to certain paths
    // In this case, it excludes the paths that start with "/auth/"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
