package com.example.demooauth2.controller.server.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/oauth")
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class OAuth2Controller {

    @Autowired
    private WhitelabelApprovalEndpoint approvalEndPoint;

    @Autowired
    private WhitelabelErrorEndpoint whitelabelErrorEndpoint;

    @Autowired
    private AuthorizationEndpoint authorizationEndpoint;

    //http://localhost:4200/oauth/authorize?client_id=mobile&response_type=code&redirect_uri=http://localhost:8082/oauth/callback&scope=WRITE&code=..

    @RequestMapping(value = "/authorize")
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus,
                                  HttpSession session) {
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        if (authentication == null || !authentication.isAuthenticated()) {
            session.setAttribute("client_id", parameters.getOrDefault("client_id", null));
            session.setAttribute("response_type", parameters.getOrDefault("response_type", null));
            session.setAttribute("redirect_uri", parameters.getOrDefault("redirect_uri", null));
            session.setAttribute("scope", parameters.getOrDefault("scope", null));
            session.setAttribute("state", parameters.getOrDefault("state", null));
            return new ModelAndView("redirect:/login");
        }
        return authorizationEndpoint.authorize(model, parameters, sessionStatus, authentication);
    }

    @RequestMapping(
            value = {"/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"}
    )
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus,
                              HttpSession session) {
        Authentication authentication = (Authentication) session.getAttribute("authentication");
        return authorizationEndpoint.approveOrDeny(approvalParameters, model, sessionStatus, authentication);
    }

    @RequestMapping("/confirm_access")
    public ModelAndView customConfirmAccessPage(Map<String, Object> model, HttpServletRequest request) throws Exception {
        // TODO: custom code here
        return approvalEndPoint.getAccessConfirmation(model, request);
    }

    @RequestMapping("/error")
    public ModelAndView customErrorPage(HttpServletRequest request) {
        // TODO: custom code here
        return whitelabelErrorEndpoint.handleError(request);
    }
}
