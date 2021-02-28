package com.example.demooauth2.controller;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import com.example.demooauth2.service.OAuth2Service;
import com.example.demooauth2.service.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
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

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientDetailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientDetailsService clientDetailsService;

    @Test
    public void createClientDetailSuccess() throws Exception {
        ClientDetailViewModel clientDetailViewModel = new ClientDetailViewModel();
        clientDetailViewModel.setClientId("clientid");
        List<String> redirectURL =  new ArrayList<>();
        clientDetailViewModel.setRedirectUri(redirectURL);

        Mockito.when(clientDetailsService.createClientDetail(Mockito.any(Principal.class), Mockito.anyString(), Mockito.anyList())).thenReturn(new CommandResult().Succeed());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(clientDetailViewModel);
        MvcResult mvcResult =  mockMvc.perform(post("/api/client/createClientDetail")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

    }

    @Test
    public void updateRedirectUriSuccess() throws Exception {
        ClientDetailViewModel clientDetailViewModel = new ClientDetailViewModel();
        clientDetailViewModel.setClientId("clientid");
        List<String> redirectURL =  new ArrayList<>();
        clientDetailViewModel.setRedirectUri(redirectURL);

        Mockito.when(clientDetailsService.updateRedirectUri(Mockito.anyString(),Mockito.anyList(),Mockito.any(Principal.class))).thenReturn(new CommandResult().Succeed());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(clientDetailViewModel);
        MvcResult mvcResult =  mockMvc.perform(post("/api/client/updateRedirectUri")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

    }

    @Test
    public void updateClientSecretSuccess() throws Exception {
        ClientDetailViewModel clientDetailViewModel = new ClientDetailViewModel();
        clientDetailViewModel.setClientSecret("clientSecret");
        List<String> redirectURL =  new ArrayList<>();
        clientDetailViewModel.setClientId("clientID");

        Mockito.when(clientDetailsService.updateClientSecret(Mockito.anyString(),Mockito.any(Principal.class))).thenReturn(new CommandResult().Succeed());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(clientDetailViewModel);
        MvcResult mvcResult =  mockMvc.perform(post("/api/client/updateClientSecret")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

    }

    @Test
    public void deleteClientDetailSuccess() throws Exception {
        ClientDetailViewModel clientDetailViewModel = new ClientDetailViewModel();
        clientDetailViewModel.setClientSecret("clientSecret");
        List<String> redirectURL =  new ArrayList<>();
        clientDetailViewModel.setClientId("clientID");

        Mockito.when(clientDetailsService.removeClientDetail(Mockito.anyString(),Mockito.any(Principal.class))).thenReturn(new CommandResult().Succeed());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(clientDetailViewModel);
        MvcResult mvcResult =  mockMvc.perform(post("/api/client/deleteClientDetail")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

    }

    @Test
    public void getClientsByUserIdSuccess() throws Exception {
        ClientDetailViewModel clientDetailViewModel = new ClientDetailViewModel();
        clientDetailViewModel.setClientSecret("clientSecret");
        List<String> redirectURL =  new ArrayList<>();
        clientDetailViewModel.setClientId("clientID");
        List<ClientDetailViewModel> clients = new ArrayList<ClientDetailViewModel>();
        Mockito.when(clientDetailsService.getClientsByUserId(Mockito.any(Principal.class))).thenReturn(new CommandResult().Succeed());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(clientDetailViewModel);
        MvcResult mvcResult =  mockMvc.perform(get("/api/client/getClientsByUserId")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
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
}
