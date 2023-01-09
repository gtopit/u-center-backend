package com.gtop.uc.oauth2.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author hongzw
 */
public class UcClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {
    private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

    private boolean allowOnlyPost = false;

    private static final String REQUEST_METHOD_POST="post";
    public UcClientCredentialsTokenEndpointFilter() {
        this("/oauth/token");
    }

    public UcClientCredentialsTokenEndpointFilter(String path) {
        super(path);
        setRequiresAuthenticationRequestMatcher(new ClientCredentialsTokenEndpointFilter.ClientCredentialsRequestMatcher(path));
        // If authentication fails the type is "Form"
        ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint).setTypeName("Form");
    }

    @Override
    public void setAllowOnlyPost(boolean allowOnlyPost) {
        this.allowOnlyPost = allowOnlyPost;
    }

    /**
     * @param authenticationEntryPoint the authentication entry point to set
     */
    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (allowOnlyPost && !REQUEST_METHOD_POST.equalsIgnoreCase(request.getMethod())) {
            try {
                throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { REQUEST_METHOD_POST });
            } catch (HttpRequestMethodNotSupportedException e) {
                e.printStackTrace();
            }
        }

        String clientId = request.getParameter("client_id");
        String clientSecret = request.getParameter("client_secret");

        // If the request is already authenticated we can assume that this
        // filter is not needed
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }

        if (clientId == null) {
            throw new BadCredentialsException("No client credentials presented");
        }

        if (clientSecret == null) {
            clientSecret = "";
        }

        clientId = clientId.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId,
                clientSecret);

        return this.getAuthenticationManager().authenticate(authRequest);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

}
