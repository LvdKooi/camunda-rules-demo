package nl.kooi.camundarules.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.api.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestUtils {

    private final ObjectMapper objectMapper;

    private static final String REQUEST_ATTRIBUTE_APP_CONTEXT = "UserContext";


    public void storeContextInformationAsRequestAttribute(HttpServletRequest request) throws IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute(REQUEST_ATTRIBUTE_APP_CONTEXT,
                    objectMapper.readValue(request.getReader(), UserDto.class));
        }
    }

    public Optional<UserDto> getContextInformation() {
        return getAttributeFromRequest(REQUEST_ATTRIBUTE_APP_CONTEXT, UserDto.class);
    }

    private <T> Optional<T> getAttributeFromRequest(String attributeName, Class<T> type) {
        Object attribute = RequestContextHolder.getRequestAttributes().getAttribute(attributeName, 0);
        if (attribute != null && type.isAssignableFrom(attribute.getClass())) {
            return Optional.of((T) attribute);
        }
        return Optional.empty();
    }

}