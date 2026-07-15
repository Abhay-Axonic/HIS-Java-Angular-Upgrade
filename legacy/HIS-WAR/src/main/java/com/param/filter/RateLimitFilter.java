package com.param.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RateLimitFilter implements Filter{

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {

        Bandwidth limit = Bandwidth.classic(
                20, // max requests
                Refill.greedy(5, Duration.ofMinutes(1))
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String ip = httpRequest.getRemoteAddr();

        String uri = httpRequest.getRequestURI();

        // Apply rate limit only for specific APIs
        if (uri.contains("/userLogin") ||
                uri.contains("/getUnitByCredentials")) {

            Bucket bucket = cache.computeIfAbsent(
                    ip,
                    k -> createNewBucket()
            );

            if (!bucket.tryConsume(1)) {

                httpResponse.setStatus(429);

                httpResponse.getWriter()
                        .write("Too many requests");

                return;
            }
        }

        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {

    }
}