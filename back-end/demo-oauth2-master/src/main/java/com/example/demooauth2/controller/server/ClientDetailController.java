package com.example.demooauth2.controller.server;

import com.example.demooauth2.modelView.clientDetail.ClientDetailViewModel;
import com.example.demooauth2.responseModel.CommandResult;
import com.example.demooauth2.service.ClientDetailsService;
import com.example.demooauth2.service.impl.ClientDetailsSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/client")
public class ClientDetailController {
    @Autowired
    private ClientDetailsService ClientDetailsSevice;

    @PostMapping("/createClientDetail")
    public ResponseEntity<Object> createClientDetail(@RequestBody ClientDetailViewModel clientDetail, Principal principal){
        CommandResult result = ClientDetailsSevice.createClientDetail(principal,clientDetail.getClientId(),clientDetail.getRedirectUri());
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @PostMapping("/updateRedirectUri")
    public ResponseEntity<Object> updateRedirectUri(@RequestBody ClientDetailViewModel clientDetail, Principal principal){
        CommandResult result = ClientDetailsSevice.updateRedirectUri(clientDetail.getClientId(),clientDetail.getRedirectUri(),principal);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @PostMapping("/updateClientSecret")
    public ResponseEntity<Object> updateClientSecret(@RequestBody Map<String, String> clientDetail, Principal principal){
        CommandResult result = ClientDetailsSevice.updateClientSecret(clientDetail.get("clientId"),principal);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @PostMapping("/deleteClientDetail")
    public ResponseEntity<Object> deleteClientDetail(@RequestBody Map<String, String> clientDetail, Principal principal){
        CommandResult result = ClientDetailsSevice.removeClientDetail(clientDetail.get("clientId"),principal);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @GetMapping("/getClientsByUserId")
    public ResponseEntity<Object> getClientsByUserId(Principal principal){
        CommandResult result = ClientDetailsSevice.getClientsByUserId(principal);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }

    @GetMapping("/getClients/{sortType}/{pageIndex}/{pageSize}")
    @PreAuthorize("hasAuthority('read_client')")
    public ResponseEntity<Object> getClients(
            @PathVariable(value = "sortType") int sortType,
            @PathVariable(value = "pageIndex") int pageIndex,
            @PathVariable(value = "pageSize") int pageSize,
            @RequestParam(required = false, defaultValue = "") String searchString){
        CommandResult result = ClientDetailsSevice.getAllClients(searchString, sortType,pageIndex,pageSize);
        return new ResponseEntity<>(result.getData(),result.getStatus());
    }
}
