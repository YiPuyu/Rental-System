package com.example.shangting.security;

import com.example.shangting.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 登录和注册接口直接放行
        if (path.startsWith("/api/login") || path.startsWith("/api/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从 Header 获取 token
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("❌ 未携带 JWT Token：" + path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // 去掉 "Bearer "
        String username = jwtUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token)) {
                // ✅ Token 验证成功，设置用户认证信息
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // ✅ 打印用户信息和角色
                System.out.println("✅ JWT 验证成功");
                System.out.println("用户: " + userDetails.getUsername());
                System.out.println("角色: " + userDetails.getAuthorities());
            } else {
                System.out.println("❌ JWT 验证失败或过期");
            }
        }

        filterChain.doFilter(request, response);
    }
}
