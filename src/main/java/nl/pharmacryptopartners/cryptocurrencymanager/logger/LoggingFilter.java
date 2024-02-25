package nl.pharmacryptopartners.cryptocurrencymanager.logger;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());
    private FileHandler fileHandler;
    private final String messageTemplate = "request: %s %s %s with body (%s) resulted in status: %s";

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {
        var logFilePath = "src/main/java/nl/pharmacryptopartners/cryptocurrencymanager/logger/logfile.log";
        try {
            fileHandler = new FileHandler(logFilePath);
            logger.addHandler(fileHandler);

            var formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    @Override
    public void destroy() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        var wrappedRequest = new ContentCachingRequestWrapper(httpRequest);

        filterchain.doFilter(wrappedRequest, response);

        var requestBody = wrappedRequest.getContentAsByteArray();
        var requestBodyAsString = new String(requestBody, wrappedRequest.getCharacterEncoding());

        var formattedMessage = String.format(
                messageTemplate,
                httpRequest.getMethod(),
                httpRequest.getRequestURL(),
                httpRequest.getQueryString(),
                requestBodyAsString,
                httpResponse.getStatus()
        );
        logger.info(formattedMessage);
    }
}
