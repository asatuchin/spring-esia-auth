package com.example.das_auth_providers.das_emulation.controller;

import com.example.das_auth_providers.common.exception.AccountLinkingException;
import com.example.das_auth_providers.common.service.ThirdPartyRegistrationService;
import com.example.das_auth_providers.das_emulation.entity.RegistrationParameters;
import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.service.SecurityService;
import com.example.das_auth_providers.das_emulation.service.UserService;
import com.example.das_auth_providers.das_emulation.validation.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserValidator userValidator;

    private final Map<String, ThirdPartyRegistrationService> thirdPartyRegistrationServices;

    public UserController(
            final UserService userService,
            final SecurityService securityService,
            final UserValidator userValidator,
            final List<ThirdPartyRegistrationService> thirdPartyRegistrationServices
    ) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;

        this.thirdPartyRegistrationServices = thirdPartyRegistrationServices.stream()
                .collect(Collectors.toMap(s -> s.getProvider().getName(), s -> s));
    }

    @GetMapping("/registration")
    public String registration(
            Model model,
            @RequestParam(value = RegistrationParameters.EMAIL, required = false) String email,
            @RequestParam(value = RegistrationParameters.THIRD_PARTY_ID, required = false) String thirdPartyId,
            @RequestParam(value = RegistrationParameters.THIRD_PARTY_PROVIDER, required = false) String thirdPartyProvider
    ) {
        User user = new User();
        if (email != null) {
            user.setEmail(email);
        }
        model.addAttribute("userForm", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(
            final @ModelAttribute("userForm") User userForm,
            final BindingResult bindingResult
    ) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(userForm);

        ThirdPartyRegistrationService linkService = thirdPartyRegistrationServices.get(
                userForm.getThirdPartyProvider()
        );
        if (linkService != null) {
            try {
                linkService.createUserLink(userForm.getId(), userForm.getThirdPartyId());
            } catch (AccountLinkingException e) {
                return "registration";
            }
        }
        securityService.autoLogin(userForm.getEmail(), userForm.getPasswordConfirm());
        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(final Model model, final String error, final String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(final Model model) {
        return "welcome";
    }
}
