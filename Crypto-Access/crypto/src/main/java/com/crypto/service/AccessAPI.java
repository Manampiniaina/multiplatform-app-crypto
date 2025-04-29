package com.crypto.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.crypto.config.AccessAPIConfig;
import com.crypto.model.reponse.JsonResponse;
import com.crypto.model.utilisateur.Genre;
import com.crypto.model.utilisateur.Utilisateur;
import com.crypto.service.util.Wrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class AccessAPI {
    
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate; // Injection du Bean RestTemplate

    public String callSymfonyService() {

       String url = AccessAPIConfig.BASE_URL+"/api/utilisateurs" ;
       try {

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {

            return "Erreur: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        }

    }

    public Genre[] listeGenres() throws Exception{

        String url = AccessAPIConfig.BASE_URL+"/genres" ;
        try {
 
            ResponseEntity<Genre[]> response = restTemplate.getForEntity(url, Genre[].class);
            return response.getBody();
            
        } catch (HttpClientErrorException | HttpServerErrorException e) {
 
            throw e ;
        }
 
     }


    public JsonResponse<Object> connection(Connection connection, Utilisateur utilisateur) throws Exception {
        
        String url = AccessAPIConfig.BASE_URL+"api/auth/login"; 
        // return appelerSymfony(url, utilisateur);
        JsonResponse<Object> rep = appelerSymfony(url, utilisateur);
        Object data = rep.getData();

        // Vérifie si 'data' est de type Utilisateur
        if (data instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;

            if (dataMap.containsKey("utilisateur")) {  // Vérifier la clé utilisateur
                // Convertir en Utilisateur si 'data' est un Utilisateur
                Utilisateur u = objectMapper.convertValue(dataMap.get("utilisateur"), Utilisateur.class);
                rep.setData(u);  // Remplacer 'data' par l'objet Utilisateur
            } else  if (dataMap.containsKey("message")){
                rep.setData(dataMap);
            }
        } 

        return rep ;
       
    }

    public JsonResponse verification(Connection connection, String id, String pin) throws Exception {
        
        String url = AccessAPIConfig.BASE_URL+"api/auth/check-pin"; 

        Map<String,String> map = new HashMap<>();
        map.put("id_compte", id);
        map.put("pin", pin);

        return appelerSymfony(url, map);
       
    }

    public JsonResponse inscription(Connection connction, Utilisateur utilisateur) throws Exception {
        
        String url = AccessAPIConfig.BASE_URL+"api/utilisateur"; 
        JsonResponse<Object> rep = appelerSymfony(url, utilisateur);
        Object data = rep.getData();

        // Vérifie si 'data' est de type Utilisateur
        if (data instanceof LinkedHashMap) {
            LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) data;

            if (dataMap.containsKey("utilisateur")) {  // Vérifier la clé utilisateur
                // Convertir en Utilisateur si 'data' est un Utilisateur
                Utilisateur u = objectMapper.convertValue(dataMap.get("utilisateur"), Utilisateur.class);
                rep.setData(utilisateur);  // Remplacer 'data' par l'objet Utilisateur
            } else  if (dataMap.containsKey("message")){
                rep.setData(objectMapper.convertValue(dataMap.get("message"), String.class));
            }
        } 

        return rep ;
    }

    private JsonResponse appelerSymfony(String url, Object objet) throws Exception{

        try {
            String jsonBody = (new Wrapper()).enJSON(objet);
        
            // Headers pour la requête
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            // Créer une entité avec le corps et les headers
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
    
            // Effectuer une requête POST
            ResponseEntity<JsonResponse<Object>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity,  new ParameterizedTypeReference<JsonResponse<Object>>() {});
            return responseEntity.getBody();

        } catch (Exception e) {
            throw e ;
        }
    }
}