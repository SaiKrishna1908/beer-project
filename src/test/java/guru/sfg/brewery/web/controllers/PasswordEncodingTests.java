package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTests {

    static final String PASSWORD = "password";


    @Test
    void hashingExample(){

        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        //salt is just some noise added to data
        String salted= PASSWORD+"some salt";

        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }

    @Test
    void testNoOp(){

        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();

        System.out.println(encoder.encode(PASSWORD));

    }


    @Test
    void testLdap(){

        PasswordEncoder encoder = new LdapShaPasswordEncoder();

        System.out.println(encoder.encode("tiger"));
//        System.out.println(encoder.encode(PASSWORD));

        assertTrue(encoder.matches(PASSWORD, encoder.encode(PASSWORD)));
    }

    @Test
    void testSha256(){
        PasswordEncoder encoder = new StandardPasswordEncoder();

        //will add salt
        String encodedPassword = encoder.encode(PASSWORD);

        System.out.println(encodedPassword);

        assertTrue(encoder.matches(PASSWORD, encodedPassword));

    }

    @Test
    void testBcrypt(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode(PASSWORD));
    }

    @Test
    void testBcypt15(){
        PasswordEncoder encoder = new BCryptPasswordEncoder(15);

        System.out.println(encoder.encode("tiger"));



    }
}
