package guru.sfg.brewery.config;

import guru.sfg.brewery.security.CustomPasswordEncoder;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestParamAuthFilter;
import org.hibernate.boot.archive.scan.internal.NoopEntryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled =  true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager manager) {

        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(manager);

        return filter;
    }

    RestParamAuthFilter restParamAuthFilter(AuthenticationManager manager) {

        RestParamAuthFilter filter = new RestParamAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(manager);

        return filter;
    }

    @Bean
    PasswordEncoder delegatingPasswordEncoder() {
        return CustomPasswordEncoder.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.addFilterBefore(restHeaderAuthFilter(authenticationManager())
                , UsernamePasswordAuthenticationFilter.class).csrf().disable();


        http.addFilterAfter(restParamAuthFilter(authenticationManager()),
                RestHeaderAuthFilter.class);

        http.authorizeRequests(authorize -> {
            authorize.antMatchers("/", "/webjars/**", "/resources/**").permitAll()
                    .antMatchers("/beers/find", "/beers*").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                    .hasAnyRole("ADMIN","CUSTOMER")
                    .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                    .hasAnyRole("ADMIN","CUSTOMER")
                    .antMatchers("/h2-console/**").permitAll()
//                    .mvcMatchers(HttpMethod.DELETE,"/api/v1/beer/**").hasRole("ADMIN")
                    .mvcMatchers(HttpMethod.GET, "/brewery/breweries","/brewery/breweries/index"
                                , "/brewery/breweries/index.html","/brewery/breweries.html")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                    .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries").hasAnyRole("ADMIN",
                    "CUSTOMER");


                //can also use mvc matcher
        })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();


                //for h2 console
                http.headers().frameOptions().sameOrigin();
    }

//     Creating users in memory

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("krishna").password("{ldap}password").roles("ADMIN");
//
//        auth.inMemoryAuthentication().withUser("qwerty")
//                .password("{sha256}3befc990abe919936c73c9df26002483b80bcde68f4324232b30b30a23cf5a709e6699e53fd0b522")
//                .roles("USER")
//                .and()
//                .withUser("scott")
//                .password("{bcrypt15}$2a$15$VhC/FGEZNRr6vri06UXJYeCEhRfTCCeOMo2OS/kkIL5HoACTg.hvu").roles("CUSTOMER");
//
//
//    }

    /*

        Alternate way to authenticate users

        @Override
        @Bean
        protected UserDetailsService userDetailsService() {
            UserDetails admin = User.withDefaultPasswordEncoder().username("krishna").password("password")
                                .roles("ADMAIN").build();

            UserDetails user = User.withDefaultPasswordEncoder().username("qwerty").password("password")
                            .roles("USER").build();

            return new InMemoryUserDetailsManager(admin, user);
        }
    */

    /*
      @Bean
    PasswordEncoder passwordEncoder(){
        return   NoOpPasswordEncoder.getInstance();
    }
*/

/*
    LDAP Password Encoder
    @Bean
    PasswordEncoder ldapPasswordEncoder(){
        return new LdapShaPasswordEncoder();
    }


 */

/*
    @Bean
    PasswordEncoder standardPasswordEncoder(){
        return new StandardPasswordEncoder();
    }
*/

/*
    @Bean
    PasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
 */


}
