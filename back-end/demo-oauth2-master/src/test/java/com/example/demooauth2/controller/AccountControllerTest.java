package com.example.demooauth2.controller;

import com.example.demooauth2.modelEntity.PasswordEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.OAuth2Service;
import com.example.demooauth2.service.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

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
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

 //   @Test
//
//    public void test_get_all_success() throws Exception {
//        List<UserEntity> users = new ArrayList<UserEntity>();
//        users.add(new UserEntity());
//
//        Mockito.when(userService.findAll()).thenReturn(users);
//
//       MvcResult mvcResult =  mockMvc.perform(get("/api/account")
//               .header("Authorization", "Bearer " +  getAccessToken("krish", "krish")))
//               .andExpect(status().isOk())
//               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//               .andExpect(jsonPath("$", hasSize(1))) // Hi vọng server trả về List độ dài 1
//               .andExpect(jsonPath("$[0].id", is(0))) // Hi vọng phần tử trả về đầu tiên có id = 1
//               .andExpect(jsonPath("$[0].username", is("test")))// Hi vọng phần tử trả về đầu tiên có name = "title-0"
//               .andDo(print()).andReturn();
//           MockHttpServletResponse res = mvcResult.getResponse();
//
//
//        ObjectMapper mapper = new ObjectMapper();
//        List<UserEntity> actual = mapper.readValue(res.getContentAsString(),new TypeReference<List<UserEntity>>(){});
//
//        Mockito.verify(userService, Mockito.times(1)).findAll();
//        Mockito.verifyNoMoreInteractions(userService);
//    }

    @Test
    public void testRegisterSuccess() throws  Exception {
        UserEntity usr = new UserEntity();
        usr.setPassword("password");
        Mockito.when(userService.registerNewUserAccount(Mockito.any(UserEntity.class))).thenReturn(new CommandResult().Succeed());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(usr);
        MvcResult mvcResult =  mockMvc.perform(post("/api/account/register").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Mockito.verify(userService, Mockito.times(1)).registerNewUserAccount(Mockito.any(UserEntity.class));
        Mockito.verifyNoMoreInteractions(userService);
    }
@Test
    public void testRegisterFail() throws  Exception {
        UserEntity usr = new UserEntity();
        Mockito.when(userService.registerNewUserAccount(Mockito.any(UserEntity.class))).thenReturn(new CommandResult(HttpStatus.INTERNAL_SERVER_ERROR, "BAD REQUEST"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(usr);
        MvcResult mvcResult =  mockMvc.perform(post("/api/account/register").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()).andReturn();

        Mockito.verify(userService, Mockito.times(1)).registerNewUserAccount(Mockito.any(UserEntity.class));
        Mockito.verifyNoMoreInteractions(userService);
    }
//    @Test
//    public void  testAddRoleSuccess() throws Exception {
//        Mockito.when(userService.addRole(Mockito.any(Principal.class),Mockito.anyString(), Mockito.anyInt())).thenReturn(new CommandResult().Succeed());
//
//        MvcResult mvcResult =  mockMvc.perform(post("/api/account/roles/0")
//                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//        Mockito.verify(userService, Mockito.times(1)).addRole(Mockito.any(Principal.class),Mockito.anyString(), Mockito.anyInt());
//        Mockito.verifyNoMoreInteractions(userService);
//
//    }

//    @Test
//    public  void testRemoveRoleSuccess() throws Exception {
//
//    }

    @Test
    public void testGetProfileSuccess() throws  Exception {
        UserEntity usr = new UserEntity();
        Mockito.when(userService.getProfile(Mockito.any(Principal.class))).thenReturn(new CommandResult().SucceedWithData(usr));

        MvcResult mvcResult =  mockMvc.perform(get("/api/account/profile")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse res = mvcResult.getResponse();

        Mockito.verify(userService, Mockito.times(1)).getProfile(Mockito.any(Principal.class));
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void updateProfileSuccess() throws  Exception {
        UserEntity usr = new UserEntity();
        Mockito.when(userService.updateProfile(Mockito.any(Principal.class), Mockito.anyMap())).thenReturn(new CommandResult().SucceedWithData(usr));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(usr);

        MvcResult mvcResult =  mockMvc.perform(put("/api/account/profile")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish")).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse res = mvcResult.getResponse();

        Mockito.verify(userService, Mockito.times(1)).updateProfile(Mockito.any(Principal.class),Mockito.anyMap());
        Mockito.verifyNoMoreInteractions(userService);
    }


    @Test
    public void unAuthorUpdateProfile() throws Exception {
        UserEntity usr = new UserEntity();
        Mockito.when(userService.updateProfile(Mockito.any(Principal.class), Mockito.anyMap())).thenReturn(new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(usr);

        MvcResult mvcResult =  mockMvc.perform(put("/api/account/profile")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        Mockito.verify(userService, Mockito.times(0)).updateProfile(Mockito.any(Principal.class),Mockito.anyMap());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void unAuthorGetProfile() throws Exception {
        UserEntity usr = new UserEntity();
        Mockito.when(userService.updateProfile(Mockito.any(Principal.class), Mockito.anyMap())).thenReturn(new CommandResult(HttpStatus.UNAUTHORIZED, "Unauthenticated"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(usr);

        MvcResult mvcResult =  mockMvc.perform(get("/api/account/profile")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        Mockito.verify(userService, Mockito.times(0)).updateProfile(Mockito.any(Principal.class),Mockito.anyMap());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void changePasswordSuccess() throws Exception {
        Mockito.when(userService.changePassword(Mockito.any(Principal.class),Mockito.anyMap())).thenReturn(new CommandResult().Succeed());

        PasswordEntity password = new PasswordEntity("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(password);


        MvcResult mvcResult =  mockMvc.perform(post("/api/account/changePassword")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(userService, Mockito.times(1)).changePassword(Mockito.any(Principal.class),Mockito.anyMap());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void unAuthorChangePassword() throws Exception {
        Mockito.when(userService.changePassword(Mockito.any(Principal.class),Mockito.anyMap())).thenReturn(new CommandResult().Succeed());

        PasswordEntity password = new PasswordEntity("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(password);


        MvcResult mvcResult =  mockMvc.perform(post("/api/account/changePassword")
                .content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        Mockito.verify(userService, Mockito.times(0)).changePassword(Mockito.any(Principal.class),Mockito.anyMap());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUsersSuccess() throws Exception {
        Mockito.when(userService.getAllUsers(Mockito.any(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(new CommandResult().Succeed());

        PasswordEntity password = new PasswordEntity("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        String requestJson = mapper.writeValueAsString(password);


        MvcResult mvcResult =  mockMvc.perform(get("/api/account/getUsers/1/1/1")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish")))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(userService, Mockito.times(1)).getAllUsers(Mockito.any(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(userService);
    }

    private String getAccessToken(String username, String password) throws Exception {


        MockHttpServletResponse response = mockMvc
                .perform(post("/oauth/token")
                        .header("Authorization", "Basic "
                                + new String(Base64Utils.encode(("mobile:pin")
                                .getBytes())))
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type", "password")
                        )
                        .andDo(print()) //
                .andReturn().getResponse();

        return new ObjectMapper()
                .readValue(response.getContentAsByteArray(), AccountControllerTest.OAuthToken.class)
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