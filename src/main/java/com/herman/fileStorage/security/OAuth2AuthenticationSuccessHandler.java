package com.herman.fileStorage.security;

import com.herman.fileStorage.entity.User;
import com.herman.fileStorage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;

    public OAuth2AuthenticationSuccessHandler(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User OAuth2User = (OAuth2User) authentication.getPrincipal();

        String githubId = Objects.requireNonNull(OAuth2User.getAttribute("id")).toString();
        String username = OAuth2User.getAttribute("login");

        User user = userService.findOrCreateGithubUser(githubId, username);
        String jwt = jwtService.generateToken(username);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\": \"" + jwt + "\"}");
    }
}
