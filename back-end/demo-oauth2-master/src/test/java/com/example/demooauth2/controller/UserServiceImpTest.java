package com.example.demooauth2.controller;

import com.c4_soft.springaddons.security.oauth2.oidc.OidcIdAuthenticationToken;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.JwtTestConf;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.example.demooauth2.config.WebSecurityConfig;
import com.example.demooauth2.config.WithOAuth2Authentication;
import com.example.demooauth2.config.filters.SimpleCorsFilter;
import com.example.demooauth2.controller.server.UserController;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import com.example.demooauth2.service.impl.UserServiceImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.var;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceImpTest {
    @Autowired
    private MockMvc mockMvc;

   // private  RestTemplate restTemplate= new RestTemplate();

   // private MockRestServiceServer mockServer;
    @MockBean
    private UserService userService;


    @Before
    public void init() {
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(userController)
//                .addFilters(new SimpleCorsFilter())
//                .build();
        // mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test

    public void test_get_all_success() throws Exception {
        List<UserEntity> users = Arrays.asList( new UserEntity());

        Mockito.when(userService.findAll()).thenReturn(users);

       MvcResult mvcResult =  mockMvc.perform(get("/api/users")
               .header("Authorization", "Bearer " +  getAccessToken("krish", "kpass")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(print()).andReturn();
           MockHttpServletResponse res = mvcResult.getResponse();

//        String fooResourceUrl
//                = "http://localhost:8083/api/users";
//        ResponseEntity<List> response
//                = restTemplate.getForEntity(fooResourceUrl , List.class);
//        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Mockito.verify(userService, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(userService);
    }
    @Test
    public void testGetProfile() throws  Exception {
        UserEntity usr = new UserEntity();


        Mockito.when(userService.findByUsername("krish")).thenReturn(usr);

        MvcResult mvcResult =  mockMvc.perform(get("/api/users/profile")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "kpass")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse res = mvcResult.getResponse();

        Mockito.verify(userService, Mockito.times(1)).findByUsername("krish");
        Mockito.verifyNoMoreInteractions(userService);
    }

    private String getAccessToken(String username, String password) throws Exception {

        // EntityLogin entityLogin = new EntityLogin("luuadmin","admin", "password");
        //... more
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // String requestJson = mapper.writeValueAsString(entityLogin);

        MockHttpServletResponse response = mockMvc
                .perform(post("/oauth/token")
                        .header("Authorization", "Basic "
                                + new String(Base64Utils.encode(("mobile:pin")
                                .getBytes())))
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type", "password")
//                        .content(buildUrlEncodedFormEntity(
//                                "username", username,
//                                "password", password,
//                                "grant_type", "password"
//                        ))
                        )
                        // .content(requestJson))
                        .andDo(print()) //
                .andReturn().getResponse();

        return new ObjectMapper()
                .readValue(response.getContentAsByteArray(), OAuthToken.class)
                .accessToken;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OAuthToken {
        @JsonProperty("access_token")
        public String accessToken;
    }

    class EntityLogin {
        private  String username;
        private  String password;
        private String grant_type;

        public EntityLogin(String username, String password, String grant_type) {
            this.username = username;
            this.password = password;
            this.grant_type = grant_type;
        }
    }

    private String buildUrlEncodedFormEntity(String... params) {
        if( (params.length % 2) > 0 ) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<params.length; i+=2) {
            if( i > 0 ) {
                result.append('&');
            }
            try {
                result.
                        append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
                        append('=').
                        append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8.name()));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }
}