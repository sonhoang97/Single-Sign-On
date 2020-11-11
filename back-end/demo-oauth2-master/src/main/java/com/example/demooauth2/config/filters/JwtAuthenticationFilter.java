//package com.example.demooauth2.config.filters;
//
//import com.example.demooauth2.modelView.users.UserTokenViewModel;
//import com.example.demooauth2.repository.JWTokenRepository;
//import com.example.demooauth2.service.commons.jwtTokenProvider.JwtTokenProvider;
//import com.example.demooauth2.service.commons.jwtTokenProvider.JwtTokenProviderImpl;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.SignatureException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//@Component
//@Order(1)
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private static final String HEADER_STRING = "Authorization";
//    private static final String TOKEN_PREFIX = "Bearer ";
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private JWTokenRepository jwTokenRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        String header = req.getHeader(HEADER_STRING);
//        UserTokenViewModel userToken = new UserTokenViewModel();
//        String authToken = null;
//        if (header != null && header.startsWith(TOKEN_PREFIX)) {
//            authToken = header.replace(TOKEN_PREFIX,"");
//            try {
//                userToken = jwtTokenProvider.getUserFromJWT(authToken);
//            } catch (IllegalArgumentException e) {
//                logger.error("an error occured during getting username from token", e);
//            } catch (ExpiredJwtException e) {
//                logger.warn("the token is expired and not valid anymore", e);
//            } catch(SignatureException e){
//                logger.error("Authentication Failed. Username or Password not valid.");
//            }
//        } else {
//            logger.warn("couldn't find bearer string, will ignore the header");
//        }
//        if (userToken.isValid() && SecurityContextHolder.getContext().getAuthentication() == null) {
//            String clientIdToken = jwtTokenProvider.getClientIdFromJWT(authToken);
//            String jwtSecret = jwTokenRepository.getJwtSecret(userToken.getId(), clientIdToken);
//
//            if (jwtTokenProvider.validateToken(authToken, jwtSecret)) {
//                UserDetails user = userDetailsService.loadUserByUsername(userToken.getUsername());
//                UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        chain.doFilter(req, res);
//    }
//}
