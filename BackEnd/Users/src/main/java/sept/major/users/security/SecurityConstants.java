package sept.major.users.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public final static String USER_CODE = "USER";
    public final static String EMPLOYEE_CODE = "EMPLOYEE";
    public final static String ADMIN_CODE = "ADMIN";

    public static final RequestMatcher OPEN_ENDPOINTS = new OrRequestMatcher(
            new AntPathRequestMatcher("/users/**", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/users/token/**", HttpMethod.GET.name())
    );

    public static final List<RequestMatcher> USER_ENDPOINTS = Arrays.asList(
            new AntPathRequestMatcher("/users/username/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/users/**", HttpMethod.PATCH.name()),
            new AntPathRequestMatcher("/users/password/**", HttpMethod.PATCH.name())
    );

    public static final List<RequestMatcher> ADMIN_ENDPOINTS = Arrays.asList(
            new AntPathRequestMatcher("/users/**", HttpMethod.DELETE.name()),
            new AntPathRequestMatcher("/users/bulk/**", HttpMethod.GET.name())
    );
}
