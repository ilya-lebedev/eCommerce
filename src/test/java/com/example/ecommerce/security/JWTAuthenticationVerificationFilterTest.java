package com.example.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JWTAuthenticationVerificationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private JWTAuthenticationVerificationFilter filter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        filter = new JWTAuthenticationVerificationFilter(authenticationManager);
    }

    @Test
    public void doFilterInternalWithNullHeader() throws IOException, ServletException {
        when(request.getHeader(anyString())).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(any(), any());
    }

    @Test
    public void doFilterInternalWithEmptyHeader() throws IOException, ServletException {
        String token = JWT.create()
                .withSubject("username")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        String header = SecurityConstants.TOKEN_PREFIX + token;
        when(request.getHeader(anyString())).thenReturn(header);

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(any(), any());
    }

}
