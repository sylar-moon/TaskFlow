package com.group.security;

import com.group.dto.UserRegistrationDTO;
import com.group.service.PersonService;
import com.group.util.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Value("${oauth.redirect}")
    private String redirectPage;

    @Autowired
    PersonService personService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = createNewUserAndGetToken(authentication);
        if (token != null) {
            redirectToPage(request, response, token);

        }

    }

    private String createNewUserAndGetToken(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        if (oAuth2User != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String password = "pass";
            if (!personService.isNewPerson(email)) {
                personService.saveNewPerson(new UserRegistrationDTO(name, email, password));
            }

            return jwtTokenUtil.generateToken(personService.loadUserByUsername(email));

        }
        return null;
    }

    private void redirectToPage(HttpServletRequest request,
                                HttpServletResponse response,
                                String token) throws IOException {

        response.addHeader("Authorization", "Bearer " + token);

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            response.sendRedirect(targetUrl);
        }
        String redirectUrlWithToken = redirectPage + "?token=" + token;

        response.sendRedirect(redirectUrlWithToken);

    }

}
