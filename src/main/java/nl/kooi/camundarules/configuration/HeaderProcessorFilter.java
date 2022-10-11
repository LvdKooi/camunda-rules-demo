package nl.kooi.camundarules.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HeaderProcessorFilter extends OncePerRequestFilter {

    private final RequestUtils requestUtils;

    public HeaderProcessorFilter(RequestUtils requestUtils) {
        this.requestUtils = requestUtils;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain filterChain) throws
            IOException, ServletException {
        requestUtils.storeContextInformationAsRequestAttribute(request);
        filterChain.doFilter(request, response);
    }
}