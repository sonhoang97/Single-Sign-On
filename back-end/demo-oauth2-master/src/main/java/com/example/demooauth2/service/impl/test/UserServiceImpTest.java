package com.example.demooauth2.service.impl.test;

import com.example.demooauth2.config.filters.SimpleCorsFilter;
import com.example.demooauth2.controller.server.UserController;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.UserService;
import com.example.demooauth2.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)

@ContextConfiguration (locations = "classpath*:/spring/applicationContext*.xml")
public class UserServiceImpTest {

    private MockMvc mockMvc;


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
    }
    @Test
    public void test_get_all_success() throws Exception {
        List<UserEntity> users = Arrays.asList( new UserEntity());

        when(userService.findAll()).thenReturn(users);

       MvcResult mvcResult =  mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse res = mvcResult.getResponse();
        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }


}