package com.example.demooauth2.controller.server;

import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import com.example.demooauth2.service.impl.ClientDetailsSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api/client")
public class ClientDetailController {
    @Autowired
    private ClientDetailsService ClientDetailsSevice;

    @PostMapping("/createClientDetail")
    public ResponseEntity<Object> createClientDetail(@RequestBody Map<String, String> clientDetail){
        CommandResult result = ClientDetailsSevice.createClientDetail(clientDetail.get("clientId"),clientDetail.get("redirectUri"));
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @PostMapping("/updateRedirectUri")
    public ResponseEntity<Object> updateRedirectUri(@RequestBody Map<String, String> clientDetail){
        CommandResult result = ClientDetailsSevice.updateRedirectUri(clientDetail.get("clientId"),clientDetail.get("redirectUri"));
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @PostMapping("/updateClientSecret")
    public ResponseEntity<Object> updateClientSecret(@RequestBody Map<String, String> clientDetail){
        CommandResult result = ClientDetailsSevice.updateClientSecret(clientDetail.get("clientId"));
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }
}
