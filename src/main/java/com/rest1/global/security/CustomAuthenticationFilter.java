package com.rest1.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//시큐리티 필터 이전에 사용하는 것. 회사 직원의 역할
@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("CustomAuthenticationFilter called");
        System.out.println("hihi");
        filterChain.doFilter(request, response); //이걸 해줘야지만 위의 과정 후 이후로 지나가게 된다.
    }
}
