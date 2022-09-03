package org.apache.coyote;

import static org.apache.coyote.HttpHeaders.CONTENT_LENGTH;
import static org.apache.coyote.HttpHeaders.CONTENT_TYPE;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.apache.coyote.constant.HttpStatus;
import org.apache.coyote.constant.MediaType;

public class HttpResponse {

    private static final String BLANK = " ";
    private static final String HTTP_VERSION = "HTTP/1.1 ";

    private final String body;
    private final HttpStatus status;
    private final HttpHeaders headers;

    private HttpResponse(final String body, final HttpStatus status, final HttpHeaders headers) {
        this.body = body;
        this.status = status;
        this.headers = headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final String CHARSET_UTF_8 = ";charset=utf-8";

        private String body;
        private HttpStatus status;
        private final HttpHeaders headers;

        public Builder() {
            this.headers = new HttpHeaders(new LinkedHashMap<>());
        }

        public Builder body(final String body) {
            this.body = body;
            final byte[] responseBody = body.getBytes();

            headers.addHeader(CONTENT_TYPE, MediaType.TEXT_HTML.getValue() + CHARSET_UTF_8);
            headers.addHeader(CONTENT_LENGTH, String.valueOf(responseBody.length));

            return this;
        }

        public Builder body(final URL resource) throws IOException {
            Objects.requireNonNull(resource);

            final File file = new File(resource.getFile());
            final Path path = file.toPath();
            final byte[] responseBody = Files.readAllBytes(path);

            headers.addHeader(CONTENT_TYPE, Files.probeContentType(path) + CHARSET_UTF_8);
            headers.addHeader("Content-Length", String.valueOf(responseBody.length));

            this.body = new String(responseBody);

            return this;
        }

        public Builder status(final HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder header(final String key, final String value) {
            headers.addHeader(key, value);
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(body, status, headers);
        }
    }

    public byte[] toBytes() {
        final String response = toString();

        return response.getBytes();
    }

    @Override
    public String toString() {
        final String statusLine = HTTP_VERSION + status.getStatusCode() + BLANK + status.getStatusMessage();
        return String.join("\r\n",
                statusLine,
                headers.toString(),
                "",
                body);
    }
}
