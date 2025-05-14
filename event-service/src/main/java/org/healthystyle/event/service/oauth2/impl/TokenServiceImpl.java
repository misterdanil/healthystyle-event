package org.healthystyle.event.service.oauth2.impl;

import java.util.Map;

import org.healthystyle.event.service.oauth2.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Component
@PropertySource("classpath:oauth2.properties")
public class TokenServiceImpl implements TokenService {
	@Autowired
	private Environment env;
	@Autowired
	private RestTemplate restTemplate;

	private String clientId;
	private String clientSecret;
	private String redirectUri;

	@PostConstruct
	public void init() {
		clientId = env.getProperty("oauth2.client_id");
		clientSecret = env.getProperty("oauth2.client_secret");
		redirectUri = env.getProperty("oauth2.redirect_uri");
	}

	@Override
	public Map<String, String> getTokens(String code) {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", redirectUri);
		body.add("code", code);
		System.out.println(body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<Map<String, String>> response = restTemplate.exchange("http://localhost:3003/oauth2/token",
				HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, String>>() {
				});
		System.out.println(response);
		
		return response.getBody();

	}

}
