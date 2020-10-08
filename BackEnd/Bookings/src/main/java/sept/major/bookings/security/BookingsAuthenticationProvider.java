package sept.major.bookings.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static sept.major.bookings.security.SecurityConstants.*;

@Component
public class BookingsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${sept.major.hours.authentication.host}")
    private String host;
    @Value("${sept.major.hours.authentication.port}")
    private String port;
    @Value("${sept.major.hours.authentication.endpoint}")
    private String endpoint;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        // Not implemented
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        String token = usernamePasswordAuthenticationToken.getCredentials().toString();
        Optional<User> userOptional = validateToken(username, token);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UsernameNotFoundException(String.format("Cannot find username with username %s and token %s", username, token));
        }
    }

    private Optional<User> validateToken(String username, String token) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", token);
        headers.set("username", username);

        ResponseEntity<Map> exchange;
        try {
            exchange = restTemplate.exchange(String.format("%s:%s%s?username=%s", host, port, endpoint, username), HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        } catch (ResourceAccessException e) {
            throw new AuthenticationServiceException("Failed to access user service");
        }


        if (HttpStatus.OK.equals(exchange.getStatusCode())) {
            String userType = (String) exchange.getBody().get("userType");

            Collection<GrantedAuthority> grantedAuthorities = Collections.EMPTY_LIST;
            if (userType.equalsIgnoreCase(USER_CODE)) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(USER_CODE);
            }

            if (userType.equalsIgnoreCase(EMPLOYEE_CODE)) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(USER_CODE, EMPLOYEE_CODE);
            }

            if (userType.equalsIgnoreCase(ADMIN_CODE)) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(USER_CODE, EMPLOYEE_CODE, ADMIN_CODE);
            }

            return Optional.of(new User(username, token, true, true, true, true, grantedAuthorities));
        } else {
            return Optional.empty();
        }

    }
}
