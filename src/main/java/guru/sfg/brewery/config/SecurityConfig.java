package guru.sfg.brewery.config;

import guru.sfg.brewery.security.CustomPasswordEncoder;
import org.hibernate.boot.archive.scan.internal.NoopEntryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Bean
    PasswordEncoder delegatingPasswordEncoder(){
        return CustomPasswordEncoder.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.authorizeRequests(authorize ->{
            authorize.antMatchers("/","/webjars/**","/resources/**")
                    .permitAll().antMatchers("/beers/find", "/beers*").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                    .permitAll().mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();

        })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("krishna").password("{bcrypt}$2a$10$KaSvmBG6DCyi0UhzwDh39ONsfOEHctwsZn8MTOCYZHuj0x6aGYzB6").roles("ADMIN");

        auth.inMemoryAuthentication().withUser("qwerty").password("{sha256}3befc990abe919936c73c9df26002483b80bcde68f4324232b30b30a23cf5a709e6699e53fd0b522").roles("USER")
        .and().withUser("scott").password("{bcrypt15}$2a$15$VhC/FGEZNRr6vri06UXJYeCEhRfTCCeOMo2OS/kkIL5HoACTg.hvu").roles("CUSTOMER");


    }

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

}
