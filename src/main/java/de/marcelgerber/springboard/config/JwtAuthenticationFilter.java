package de.marcelgerber.springboard.config;

import de.marcelgerber.springboard.exception.NotFoundException;
import de.marcelgerber.springboard.model.Player;
import de.marcelgerber.springboard.repository.PlayerRepository;
import de.marcelgerber.springboard.service.PlayerService;
import de.marcelgerber.springboard.util.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Using playerRepository and not playerService here, because it would
    // form a dependency cycle (because of PasswordEncoder in playerService)
    private final PlayerRepository playerRepository;

    public JwtAuthenticationFilter(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String username = JwtUtil.getSubject(token);

        if(username != null) {
            if(JwtUtil.isTokenValid(token, username)) {
                Player player = playerRepository.findByUsername(username)
                        .orElseThrow(() -> new NotFoundException("username not found"));

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(player, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

}
