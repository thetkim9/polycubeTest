package kr.co.polycube.backendtest.component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class CustomFilter implements Filter {

    private final Pattern pattern = Pattern.compile("^[?&=:/a-zA-Z0-9]*$");
    //private final Pattern pattern = Pattern.compile("^[-.?&=:/a-zA-Z0-9]*$"); //for h2-console

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String query = httpRequest.getQueryString();
        if (query!=null)
            requestURI += query;
        
        if (!pattern.matcher(requestURI).matches()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setHeader("Content-Type", "application/json");
            String responseBody = "{\"reason\":\"URL contains invalid special characters\"}";
            httpResponse.getWriter().write(responseBody);
            httpResponse.getWriter().flush();
            return;
        }

        chain.doFilter(request, response);
    }

}
