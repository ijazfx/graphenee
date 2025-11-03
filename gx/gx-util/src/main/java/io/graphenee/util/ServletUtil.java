package io.graphenee.util;

import jakarta.servlet.http.HttpServletRequest;

public class ServletUtil {
    
    public static final String HOST_HEADER = "Host";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static String host(HttpServletRequest request) {
        return request.getServerName();
    }

}
