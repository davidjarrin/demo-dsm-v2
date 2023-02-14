package dev.djarrin.demodsm.controllers;

import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.djarrin.demodsm.models.UsersGoRest;
import dev.djarrin.demodsm.services.GoRestApiService;

@RestController
public class GoRestController {

    private GoRestApiService goRestApiService;

    public GoRestController(GoRestApiService goRestApiService) {
        this.goRestApiService = goRestApiService;
    }

    @GetMapping("/api/senhasegura/users")
    public List<UsersGoRest> getUsers(){
        List<UsersGoRest> usersGoRests = goRestApiService.getUsersGoRest();
        Random r = new Random();
        for(UsersGoRest user: usersGoRests){
            user.setSaldo(1000 + (10000 - 1000) * r.nextDouble());
        }
        return usersGoRests;
    }

}
