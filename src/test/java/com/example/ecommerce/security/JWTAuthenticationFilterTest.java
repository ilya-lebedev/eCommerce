package com.example.ecommerce.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JWTAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private Authentication authResult;

    private JWTAuthenticationFilter filter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        filter = new JWTAuthenticationFilter(authenticationManager);
    }

    @Test
    public void successfulAuthentication() throws IOException, ServletException {
        when(authResult.getPrincipal()).thenReturn(new User("username", "password", new ArrayList<>()));
        filter.successfulAuthentication(request, response, chain, authResult);
        verify(response, times(1)).addHeader(anyString(), anyString());
    }

}
