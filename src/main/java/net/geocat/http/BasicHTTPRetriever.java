package net.geocat.http;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Scope("prototype")
@Qualifier("basicHTTPRetriever")
public class BasicHTTPRetriever implements IHTTPRetriever {

    Logger logger = LoggerFactory.getLogger(BasicHTTPRetriever.class);

    int TIMEOUT_MS = 2 * 60 * 1000;

    int initialReadSize = 1000;


    public boolean shouldReadMore(byte[] tinyBuffer, IContinueReadingPredicate predicate)
    {
        if (predicate == null)
            return true;
        return predicate.continueReading(tinyBuffer);
    }

    /**
     * @param verb     GET or POST
     * @param location url
     * @param body     for POST, body
     * @param cookie   cookies to attach -   http.setRequestProperty("Cookie", cookie);
     * @return response from server
     * @throws Exception
     */
    public byte[] retrieveXML(String verb, String location, String body, String cookie, IContinueReadingPredicate predicate) throws IOException, SecurityException, ExceptionWithCookies, RedirectException {

        if (body == null)
            body = "";
        URL url = new URL(location);
        if (!url.getProtocol().equalsIgnoreCase("http") && (!url.getProtocol().equalsIgnoreCase("https")))
            throw new SecurityException("Security violation - url should be HTTP or HTTPS");

        if (!verb.equals("POST") && !verb.equals("GET"))
            throw new SecurityException("verb should be 'POST' or 'GET'");

        logger.debug("      * " + verb + " to " + location + " with body " + body.replace("\n", ""));

        byte[] body_bytes = body.getBytes(StandardCharsets.UTF_8);
        byte[] response_bytes;

        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setConnectTimeout(TIMEOUT_MS);
        http.setReadTimeout(TIMEOUT_MS);
        http.setRequestMethod(verb);
        http.setDoOutput(true);
        http.setDoInput(true);
        if (verb.equals("POST")) {
            http.setFixedLengthStreamingMode(body_bytes.length);
            http.setRequestProperty("Content-Type", "application/xml");
        }
        if ((cookie != null) && (!cookie.isEmpty()))
            http.setRequestProperty("Cookie", cookie);

        String response;
        http.connect();
        try {
            // send body
            if (body_bytes.length > 0) {
                try (OutputStream os = http.getOutputStream()) {
                    os.write(body_bytes);
                }
            }
            // get response
            try (InputStream is = http.getInputStream()) {
                byte[] tinyBuffer = new byte[initialReadSize];
                int ntinyRead = IOUtils.read(is,tinyBuffer);
                byte[] bigBuffer = new byte[0];
                if (shouldReadMore(tinyBuffer,predicate)) {
                   bigBuffer = IOUtils.toByteArray(is);
                }
                response_bytes = new byte[ntinyRead + bigBuffer.length];
                System.arraycopy(tinyBuffer,0,response_bytes,0, ntinyRead);
                System.arraycopy(bigBuffer,0,response_bytes,ntinyRead, bigBuffer.length);
                int t=0;
            }
        } catch (IOException ioException) {
            List<String> cookies = http.getHeaderFields().get("Set-Cookie");
            if ((cookies == null) || (cookies.isEmpty()))
                throw ioException;
            throw new ExceptionWithCookies(ioException.getMessage(), cookies.get(0), ioException);
        } finally {
            http.disconnect();
        }

        if (http.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || http.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            String newUrl = http.getHeaderField("Location");
            throw new RedirectException("redirect requested", newUrl);
        }
        //logger.debug("      * FINISHED " + verb + " to " + location + " with body " + body.replace("\n", ""));
        return response_bytes;
    }

}
