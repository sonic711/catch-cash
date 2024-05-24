package com.sean.web.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Slf4j
@Service
public class ResTemplateService {

    public <T> Object getJsonObject(String url, Class<T> responseType) throws Exception {
        try {
            log.debug("getJsonObject url = {}", url);
            RestTemplate restTemplate = extracted(url);
            ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
            return new Gson().fromJson(res.getBody(), responseType);
        } catch (Exception e) {
            log.error("getJsonObject occurs exception : {}", e.getLocalizedMessage());
            throw e;
        }
    }

    private RestTemplate extracted(String url) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(1500));
        requestFactory.setReadTimeout(Duration.ofMillis(1500));
        if (!isHttps(url)) {
            return restTemplateBuilder.requestFactory(() -> requestFactory).build();
        } else {
            HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
            CloseableHttpClient httpClient = HttpClients.custom()//
                    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()//
                            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()//
                                    .setSslContext(SSLContextBuilder.create()//
                                            .loadTrustMaterial(TrustAllStrategy.INSTANCE).build())//
                                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE).build()).build()).build();
            customRequestFactory.setHttpClient(httpClient);
            customRequestFactory.setConnectTimeout(Duration.ofMillis(1500));

            return restTemplateBuilder.requestFactory(() -> customRequestFactory).build();
        }
    }

    private Boolean isHttps(String targetUrl) {
        return StringUtils.equalsIgnoreCase(UriComponentsBuilder.fromHttpUrl(targetUrl).build().getScheme(), "https") ? Boolean.TRUE : Boolean.FALSE;
    }

}
