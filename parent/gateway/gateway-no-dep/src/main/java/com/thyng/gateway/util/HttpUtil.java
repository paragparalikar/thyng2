package com.thyng.gateway.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpUtil {
	
	public static HttpURLConnection build(String url, String username, String password) throws MalformedURLException, IOException{
		final String encoded = Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8));
		final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
		connection.setRequestProperty("Authorization", "Basic "+encoded);
		return connection;
	}

}
