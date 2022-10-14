package nl.kooi.camundarules.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.kooi.camundarules.api.dto.UserDto;
import org.apache.tomcat.util.codec.binary.Base64;
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
    private static final String HEADER = "X-Auth-User";

    public void storeContextInformationAsRequestAttribute(HttpServletRequest request) {
        Optional.ofNullable(request.getHeader(HEADER))
                .map(Base64::decodeBase64)
                .map(this::getUserDtoFromDecodedString)
                .ifPresent(user -> request.setAttribute(REQUEST_ATTRIBUTE_APP_CONTEXT, user));
    }


    private UserDto getUserDtoFromDecodedString(byte[] decodedUser) {
        try {
            return objectMapper.readValue(decodedUser, UserDto.class);
        } catch (IOException e) {
            return null;
        }
    }

    public Optional<UserDto> getContextInformation() {
        return getAttributeFromRequest(REQUEST_ATTRIBUTE_APP_CONTEXT, UserDto.class);
    }

    private <T> Optional<T> getAttributeFromRequest(String attributeName, Class<T> type) {
        Object attribute = RequestContextHolder.getRequestAttributes().getAttribute(REQUEST_ATTRIBUTE_APP_CONTEXT, 0);
        if (attribute != null && type.isAssignableFrom(attribute.getClass())) {
            return Optional.of((T) attribute);
        }
        return Optional.empty();
    }

}