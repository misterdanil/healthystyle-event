package org.healthystyle.event.service.oauth2;

import java.util.Map;

public interface TokenService {
	public Map<String, String> getTokens(String code);
}
