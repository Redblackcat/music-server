package com.guoran;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestSecurity {
    @Test
    public void TestBcryptPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String encode1 = bCryptPasswordEncoder.encode("1234");
        String encode2 = bCryptPasswordEncoder.encode("1234");
        //原文相同，密文不同
        System.out.println(encode1);
        System.out.println(encode2);

        //结果为true
        System.out.println(bCryptPasswordEncoder.matches("1234", "$2a$10$/EBrIqivB1j1MRlRKOPA3OVpLxw.FDXM4EW3WSyThfcr1QvuW0B3q"));
        //结果为false
        System.out.println(bCryptPasswordEncoder.matches("12345", "$2a$10$/EBrIqivB1j1MRlRKOPA3OVpLxw.FDXM4EW3WSyThfcr1QvuW0B3q"));


    }
}
