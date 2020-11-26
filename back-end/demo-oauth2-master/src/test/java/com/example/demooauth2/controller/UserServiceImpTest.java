package com.example.demooauth2.controller;

import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
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

    @MockBean
    private UserService userService;


    @Before
    public void init() {
    }

    @Test

    public void test_get_all_success() throws Exception {
        List<UserEntity> users = new ArrayList<UserEntity>();
        users.add(new UserEntity());

        Mockito.when(userService.findAll()).thenReturn(users);

       MvcResult mvcResult =  mockMvc.perform(get("/api/users")
               .header("Authorization", "Bearer " +  getAccessToken("krish", "kpass")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(1))) // Hi vọng server trả về List độ dài 1
               .andExpect(jsonPath("$[0].id", is(0))) // Hi vọng phần tử trả về đầu tiên có id = 1
               .andExpect(jsonPath("$[0].username", is("test")))// Hi vọng phần tử trả về đầu tiên có name = "title-0"
               .andDo(print()).andReturn();
           MockHttpServletResponse res = mvcResult.getResponse();


        ObjectMapper mapper = new ObjectMapper();
        List<UserEntity> actual = mapper.readValue(res.getContentAsString(),new TypeReference<List<UserEntity>>(){});

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