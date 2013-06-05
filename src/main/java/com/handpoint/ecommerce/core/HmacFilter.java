package com.handpoint.ecommerce.core;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * All requests sent to the Handpoint E-Commerce interface need to be authenticated using
 * HMAC. This Jersey client filter calculates the HMAC and adds it to the request.
 *
 * @author palmithor
 * @since 2013-03
 */
public class HmacFilter extends ClientFilter {

    public static final String HMAC_SHA_1 = "HmacSHA1";
    public static final String UTF_8 = "UTF-8";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private String sharedSecret;

    static final byte[] HEX_CHAR_TABLE = {
            (byte) '0', (byte) '1', (byte) '2', (byte) '3',
            (byte) '4', (byte) '5', (byte) '6', (byte) '7',
            (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };


    public HmacFilter(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }


    @Override
    public ClientResponse handle(ClientRequest request) throws ClientHandlerException {

        addHeaders(request);
        ClientResponse response = getNext().handle(request);

        return response;
    }

    /**
     * Adds headers needed for authentication
     *
     * @param request to be sent
     */
    private void addHeaders(ClientRequest request) {
        String now = dateFormat.format(new Date());
        String toMac = getHmacString(request, now);
        request.getHeaders().add("mws-date", now);
        request.getHeaders().add("mws-hmac", generateHmac(toMac));
        request.getHeaders().add("content-type", MediaType.APPLICATION_XML);
    }


    /**
     * Generates HMAC from a String using shared secret member variable.
     *
     * @param toMac string to hash
     * @return hashed string
     */
    private String generateHmac(String toMac) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(sharedSecret.getBytes(), HMAC_SHA_1);
            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA_1);
            mac.init(signingKey);
            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(toMac.getBytes());

            return getHexString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert bytes to hexadecimal string.
     *
     * @param raw bytes to convert
     * @return hexadecimal string - the hashed value
     * @throws UnsupportedEncodingException
     */
    private String getHexString(byte[] raw) throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;

        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, UTF_8);
    }

    /**
     * Builds the HmacString according to rules on how to create a string to hash.
     *
     * @param request to be sent
     * @param date    date string
     * @return string that will be hashed.
     */
    private String getHmacString(ClientRequest request, String date) {
        StringBuilder builder = new StringBuilder();
        builder.append(request.getMethod());
        builder.append(request.getURI().getPath());
        builder.append(date);
        if (request.getEntity() != null) {
            builder.append(new String((byte[]) request.getEntity()));
        }
        return builder.toString();
    }
}
