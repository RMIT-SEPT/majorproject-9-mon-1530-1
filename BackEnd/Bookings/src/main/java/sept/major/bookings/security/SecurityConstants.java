package sept.major.bookings.security;

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
            new AntPathRequestMatcher("/bookings/range/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/bookings/date/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/bookings/all/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/bookings/**", HttpMethod.GET.name()),
            new AntPathRequestMatcher("/bookings/**", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/bookings/**", HttpMethod.DELETE.name())
    );

    public static final List<RequestMatcher> EMPLOYEE_ENDPOINTS = Arrays.asList(
            new AntPathRequestMatcher("/bookings/**", HttpMethod.PATCH.name())
    );
}
