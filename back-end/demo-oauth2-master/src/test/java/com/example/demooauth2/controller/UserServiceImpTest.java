package com.example.demooauth2.controller;

import com.c4_soft.springaddons.security.oauth2.oidc.OidcIdAuthenticationToken;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.JwtTestConf;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.example.demooauth2.config.WebSecurityConfig;
import com.example.demooauth2.config.filters.SimpleCorsFilter;
import com.example.demooauth2.controller.server.UserController;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import com.example.demooauth2.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {
                UserController.class,
                SpringTestConfig.class,
              })
@WebMvcTest(UserController.class)
public class UserServiceImpTest {

    private MockMvc mockMvc;

   // private  RestTemplate restTemplate= new RestTemplate();

   // private MockRestServiceServer mockServer;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .addFilters(new SimpleCorsFilter())
                .build();
        // mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void test_get_all_success() throws Exception {
        List<UserEntity> users = Arrays.asList( new UserEntity());

        Mockito.when(userService.findAll()).thenReturn(users);

       MvcResult mvcResult =  mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
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
    @WithMockAuthentication(
            authType = JwtAuthenticationToken.class,
            name = "krish",
            authorities = "ROLE_USER")
    public void testGetProfile() throws  Exception {
        UserEntity usr = new UserEntity();
        Mockito.when(userService.findByUsername("krish")).thenReturn(usr);

        MvcResult mvcResult =  mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        MockHttpServletResponse res = mvcResult.getResponse();
        Mockito.verify(userService, Mockito.times(1)).findByUsername("krish");
        Mockito.verifyNoMoreInteractions(userService);
    }



}