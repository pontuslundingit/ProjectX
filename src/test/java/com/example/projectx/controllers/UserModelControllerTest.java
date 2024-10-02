package com.example.projectx.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;

class UserModelControllerTest {

    private UserModelControllerTestImpl userModelController;

    @BeforeEach
    void setUp() {
        userModelController = new UserModelControllerTestImpl();
    }

    @Test
    void testAddUserToModel_OAuth2User() {
        OAuth2User oauthUser = mock(OAuth2User.class);
        Model model = mock(Model.class);

        when(oauthUser.getAttribute("login")).thenReturn("testUser");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(oauthUser);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        userModelController.addUserToModel(model);

        verify(model).addAttribute("username", "testUser");
    }

    @Test
    void testAddUserToModel_UserDetails() {
        UserDetails userDetails = mock(UserDetails.class);
        Model model = mock(Model.class);

        when(userDetails.getUsername()).thenReturn("testUser");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        userModelController.addUserToModel(model);

        verify(model).addAttribute("username", "testUser");
    }

    @Test
    void testAddUserToModel_NoAuthentication() {
        Model model = mock(Model.class);

        SecurityContextHolder.setContext(mock(SecurityContext.class));

        userModelController.addUserToModel(model);

        verify(model, never()).addAttribute(anyString(), any());
    }
}
