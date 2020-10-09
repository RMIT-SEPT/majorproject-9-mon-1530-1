package sept.major.hours.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public final static String USER_CODE = "USER";
    public final static String EMPLOYEE_CODE = "EMPLOYEE";
    public final static String ADMIN_CODE = "ADMIN";

    public static final List<RequestMatcher> USER_ENDPOINTS = Arrays.asList(
            new AntPathRequestMatcher("/hours/range/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/hours/date/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/hours/all/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/hours/**", HttpMethod.GET.name())
    );

    public static final List<RequestMatcher> ADMIN_ENDPOINTS = Arrays.asList(
            new AntPathRequestMatcher("/hours/**", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/hours/**", HttpMethod.PATCH.name()),
            new AntPathRequestMatcher("/hours/**", HttpMethod.DELETE.name())
    );
}
