package com.example.projectx.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

public abstract class UserModelController {

    protected void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                String username = oauthUser.getAttribute("login");
                model.addAttribute("username", username);
            } else if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                model.addAttribute("username", username);
            }
        }
    }

}
