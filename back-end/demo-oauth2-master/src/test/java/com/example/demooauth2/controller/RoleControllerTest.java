package com.example.demooauth2.controller;

import com.example.demooauth2.modelEntity.PermissionEntity;
import com.example.demooauth2.modelEntity.RoleEntity;
import com.example.demooauth2.modelEntity.UserEntity;
import com.example.demooauth2.modelView.roles.RoleViewModel;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.PermissionService;
import com.example.demooauth2.service.RoleService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
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

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Test
    public  void getRoleTest() throws Exception {
        List<RoleEntity> roles = new ArrayList<RoleEntity>();
        roles.add(new RoleEntity());

        Mockito.when(roleService.getAll()).thenReturn(new CommandResult().SucceedWithData(roles));

        MvcResult mvcResult =  mockMvc.perform(get("/api/roles")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))) // Hi vọng server trả về List độ dài 1
                .andDo(print()).andReturn();

        Mockito.verify(roleService, Mockito.times(1)).getAll();
        Mockito.verifyNoMoreInteractions(roleService);
    }
    @Test
    public void createNewTest() throws Exception {
        RoleEntity permissionEx = new RoleEntity();
        Mockito.when(roleService.CreateNew(Mockito.any(RoleViewModel.class))).thenReturn(new CommandResult().SucceedWithData("Create new role successful!"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(permissionEx);
        MvcResult mvcResult =  mockMvc.perform(post("/api/roles")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Assert.assertEquals("Create new role successful!",mvcResult.getResponse().getContentAsString());

    }

    @Test
    public  void updateTest() throws  Exception {
        PermissionEntity permissionEx = new PermissionEntity("test");
        Mockito.when(roleService.Update(Mockito.anyInt(),Mockito.any(RoleEntity.class))).thenReturn(new CommandResult().SucceedWithData("Update role successful!"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        String requestJson = mapper.writeValueAsString(permissionEx);

        MvcResult mvcResult =  mockMvc.perform(put("/api/roles/0")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Assert.assertEquals("Update role successful!",mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void deleteTest() throws Exception  {
        PermissionEntity permissionEx = new PermissionEntity("test");
        Mockito.when(roleService.Delete(Mockito.anyInt())).thenReturn(new CommandResult().SucceedWithData("Delete role successful!"));

        MvcResult mvcResult =  mockMvc.perform(delete("/api/roles/0")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Assert.assertEquals("Delete role successful!", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public  void unAuthorTest() throws Exception {
        MvcResult mvcResult =  mockMvc.perform(get("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
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

    @Test
    public void addPermissionSuccess() throws Exception {
        PermissionEntity permissionEx = new PermissionEntity("test");
        Mockito.when(roleService.AddPermission(Mockito.anyInt(),Mockito.anyInt())).thenReturn(new CommandResult().SucceedWithData("Add permission to role successful!"));

        MvcResult mvcResult =  mockMvc.perform(post("/api/roles/0/permissions/0")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Assert.assertEquals("Add permission to role successful!", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void deletePermissionSuccess() throws Exception {
        PermissionEntity permissionEx = new PermissionEntity("test");
        Mockito.when(roleService.DeletePermission(Mockito.anyInt(),Mockito.anyInt())).thenReturn(new CommandResult().SucceedWithData("Delete permission to role successful!"));

        MvcResult mvcResult =  mockMvc.perform(delete("/api/roles/0/permissions/0")
                .header("Authorization", "Bearer " +  getAccessToken("krish", "krish"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Assert.assertEquals("Delete permission to role successful!", mvcResult.getResponse().getContentAsString());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OAuthToken {
        @JsonProperty("access_token")
        public String accessToken;
    }
}
