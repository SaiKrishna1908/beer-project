package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class AbstractAuthFilter extends AbstractAuthenticationProcessingFilter {

    public AbstractAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if(!requiresAuthentication(request, response)){
            chain.doFilter(request, response);

            return;
        }

        if(log.isDebugEnabled())
            log.debug("Request is to process authentication");

        try{
            Authentication authentication = attemptAuthentication(request, response);

            if(authentication != null)
                    successfulAuthentication(request,response, chain, authentication);

            else
                chain.doFilter(request,response);
        }
        catch (AuthenticationException e){
            unsuccessfulAuthentication(request, response,e);
        }



     }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException,
            ServletException {

        if(log.isDebugEnabled())
                log.debug("Authentication success. Updating SecurityContextHolder to contain: "+authResult);

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        if(log.isDebugEnabled()){
            log.debug("Authentication request failed: " + failed.toString(), failed);
            log.debug("Updated SecurityContextHolder to contain null Authentication");
        }

        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String username = getUsername(request);
        String password = getPassword(request);

        if(username == null)
                username = "";
        if(password == null)
                password = "";

        log.debug(username);
        log.debug(password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);

        if(!StringUtils.isEmpty(username)){
            return this.getAuthenticationManager().authenticate(token);
        }
        return  null;
    }

    protected abstract String getUsername(HttpServletRequest request);
    protected abstract String getPassword(HttpServletRequest request);


}
