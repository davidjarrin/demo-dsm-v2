package dev.djarrin.demodsm.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dev.djarrin.demodsm.models.UsersGoRest;

@Component
public class GoRestApiService {

    private OAuthClientSample oAuthClientSample;
    private String BearerToken;

    @Value("${api.go.rest.url}")
    private String goRestUrl;


    public GoRestApiService(OAuthClientSample oAuthClientSample) {
        this.oAuthClientSample = oAuthClientSample;
        getBearerToken();
    }

    private void getBearerToken (){
        String response = oAuthClientSample.getCredentialsSenhaSegura();

		JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
		
		this.BearerToken = jsonResponse.getAsJsonObject("application")
										.getAsJsonArray("secrets").get(0).getAsJsonObject()
										.getAsJsonArray("data").get(0).getAsJsonObject()
										.get("auth token").toString();
    }

    public List<UsersGoRest> getUsersGoRest(){
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + BearerToken);;
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = goRestUrl + "/users?page=1&per_page=40";
        ResponseEntity<UsersGoRest[]> goRestUsers = restTemplate.exchange(url, HttpMethod.GET, entity, UsersGoRest[].class);
		return Arrays.asList(goRestUsers.getBody());
    }
}
