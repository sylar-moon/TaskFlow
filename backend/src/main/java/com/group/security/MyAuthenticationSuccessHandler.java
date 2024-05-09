package com.group.security;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValues;
import com.group.dto.UserRegistrationDTO;
import com.group.service.AuthService;
import com.group.service.PersonService;
import com.group.util.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Value("${oauth.redirect}")
    private String redirectPage;


    private final PersonService personService;



    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyAuthenticationSuccessHandler(PersonService personService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

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
            String password = passwordEncoder.encode("pass");
            String userPic = (String) attributes.get("picture");
            log.info("this is userpic ={}",userPic);
            if (!personService.isNewPerson(email)) {
                personService.saveNewPerson(new UserRegistrationDTO(name, email, password,userPic));
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
