package com.github.xuqplus2.authserver.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {

    public static final String getOrigin(String url) throws MalformedURLException {
        URL url1 = new URL(url);
        int port = url1.getPort();
        if (-1 != port) {
            return String.format("%s://%s:%s", url1.getProtocol(), url1.getHost(), url1.getPort());
        } else {
            return String.format("%s://%s", url1.getProtocol(), url1.getHost());
        }
    }
}
