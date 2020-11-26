package com.example.demooauth2.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MockMvcHelper {
    @Autowired
    private MockMvc mockMvc;

    private String getAccessToken(String username, String password) throws Exception {

        // EntityLogin entityLogin = new EntityLogin("luuadmin","admin", "password");
        //... more
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//
//        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // String requestJson = mapper.writeValueAsString(entityLogin);

        MockHttpServletResponse response = mockMvc
                .perform(post("http://locahost:8083/oauth/token")
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
