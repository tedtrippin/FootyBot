package com.rob.betBot.http;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.rob.betBot.exception.CommunicatorException;

public class JsonCommunicator {

    private static Logger log = LogManager.getLogger(JsonCommunicator.class);

    private final Client client;
    private String url;
    private int lastRequestStatus;

    protected JsonCommunicator(String url) {
        this.url = url;
        client = ClientBuilder.newClient();
    }

    public int getLastRequestStatus() {
        return lastRequestStatus;
    }

    protected String get(String method, MultivaluedMap<String, Object> headers)
        throws CommunicatorException {

        Builder builder = client.target(url + "/" + method).request(MediaType.APPLICATION_JSON);
        if (headers != null && !headers.isEmpty())
            builder.headers(headers);

        Invocation invocation = builder.buildGet();

        return doRequest(invocation);
    }

    protected String post(Object postData, String method, MultivaluedMap<String, Object> headers)
        throws CommunicatorException {

        String methodUrl = url;
        if (!Strings.isNullOrEmpty(method))
            methodUrl += "/" + method;

        Builder builder = client.target(methodUrl).request(MediaType.APPLICATION_JSON);
        if (headers != null && !headers.isEmpty())
            builder.headers(headers);

        Invocation invocation = builder.buildPost(Entity.json(postData));

        return doRequest(invocation);
    }

    protected void delete(String method, MultivaluedMap<String, Object> headers)
        throws CommunicatorException {

        Builder builder = client.target(url + "/" + method).request(MediaType.APPLICATION_JSON);
        if (headers != null && !headers.isEmpty())
            builder.headers(headers);

        Invocation invocation = builder.buildDelete();

        doRequest(invocation);
    }

    private String doRequest(Invocation invocation)
        throws CommunicatorException {

        try {
            log.debug("Starting web request...");
            Response response = invocation.invoke();

            lastRequestStatus = response.getStatus();

            if (lastRequestStatus >= 300) {
                String responseBody = response.readEntity(String.class);
                log.error(responseBody);
                throw new CommunicatorException("HTTP " + lastRequestStatus);
            } else {
                log.debug("Done, status[" + lastRequestStatus + "]");
            }

            String jsonResponse = response.readEntity(String.class);
            return jsonResponse;

        } catch (ProcessingException ex) {
            throw new CommunicatorException("Coudn't process response", ex);
        }
    }
}
