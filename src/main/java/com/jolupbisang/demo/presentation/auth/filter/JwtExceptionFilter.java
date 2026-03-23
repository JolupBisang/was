package com.jolupbisang.demo.presentation.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.global.exception.ErrorCode;
import com.jolupbisang.demo.global.exception.GlobalErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            handleException(response, ex, GlobalErrorCode.EXPIRED_JWT);
        } catch (MalformedJwtException ex) {
            handleException(response, ex, GlobalErrorCode.INVALID_ACCESS_TOKEN);
        } catch (SignatureException ex) {
            handleException(response, ex, GlobalErrorCode.INVALID_TOKEN_SIGNATURE);
        } catch (JwtException ex) {
            handleException(response, ex, GlobalErrorCode.UNKNOWN_TOKEN_ERROR);
        } catch (CustomException ex) {
            handleException(response, ex, ex.getErrorCode());
        } catch (Exception ex) {
            handleException(response, ex, GlobalErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleException(HttpServletResponse response, Exception ex, ErrorCode errorCode) throws IOException {
        String errorId = UUID.randomUUID().toString();
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getMessage(), errorId);

        log.error("[{}] Filter 예외 발생: {}", errorId, errorCode.getMessage(), ex);
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private record ErrorResponse(String message, String errorId) {
    }
}
