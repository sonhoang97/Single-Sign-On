//package com.example.demooauth2.controller.server.oauth2;
//
//import com.example.demooauth2.modelView.tokens.Oauth2Token;
//import com.example.demooauth2.responseModel.CommandResult;
//import com.example.demooauth2.service.OAuth2Service;
//import com.sun.net.httpserver.Authenticator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
//import org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.support.SessionStatus;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.View;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.security.Principal;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/oauth")
//public class OAuth2Controller {
//    @Autowired
//    private AuthorizationCodeServices authorizationCodeServices;
//    @RequestMapping(value = "/authorize")
//    public CommandResult authorize(@RequestParam Map<String, String> approvalParameters, Principal principal) {
//        Authentication authentication = (Authentication) principal;
////        authorizationCodeServices.
//        return new CommandResult(HttpStatus.OK);
//    }
//}
