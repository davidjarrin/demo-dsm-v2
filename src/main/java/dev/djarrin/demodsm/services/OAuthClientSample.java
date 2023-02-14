package dev.djarrin.demodsm.services;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Component
public class OAuthClientSample {

    private String OAUTH_SERVER_URL;
    private String clientId;
    private String clientsecret;
    private String APPLICATION_SERVER_URL;
    
    public OAuthClientSample(@Value("${app.url.token}") String oAUTH_SERVER_URL, 
                             @Value("${app.client.id}") String clientId, 
                             @Value("${app.client.secret}") String clientsecret,
                             @Value("${app.url.application}") String APPLICATION_SERVER_URL) {
        this.OAUTH_SERVER_URL = oAUTH_SERVER_URL;
        this.clientId = clientId;
        this.clientsecret = clientsecret;
        this.APPLICATION_SERVER_URL = APPLICATION_SERVER_URL;
    }
    
    public String getCredentialsSenhaSegura() {

        String authToken = getToken();
        //System.out.println("Token: " + authToken);

        String authHeader = "Bearer " + authToken;

        Request request = new Request.Builder().url(APPLICATION_SERVER_URL)
        .get()
        .addHeader("Authorization", authHeader)
        .build();

        OkHttpClient.Builder newBuilder = GetBuilderNoCertCheck();

        String responseBody = "";
        try {
            Response response = newBuilder.build().newCall(request).execute();
            responseBody = response.body().string();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseBody;
    }

    private String getToken(){

        String credentials = clientId+":"+clientsecret;

        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        String access_token_url = OAUTH_SERVER_URL;
       
        String authHeader = "Basic " + encodedCredentials;
        
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("grant_type", "client_credentials")
        .build();

        Request request = new Request.Builder().url(access_token_url)
        .post(body)
        .addHeader("Authorization", authHeader)
        .addHeader("ContentType", "application/x-www-form-urlencoded")
        .build();

        String tokenResult = "";

        OkHttpClient.Builder newBuilder = GetBuilderNoCertCheck();

        try {
            Response response = newBuilder.build().newCall(request).execute();
            Map <String, Object> jsonResponse = new Gson().fromJson(response.body().string(), Map.class);
            tokenResult = (String)jsonResponse.get("access_token");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tokenResult;
        
    } 

    private OkHttpClient.Builder GetBuilderNoCertCheck(){
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }
        
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }
        
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
        };

        
       SSLContext sslContext;
       OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            return newBuilder.hostnameVerifier((hostname, session) -> true);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newBuilder;
    }

}
