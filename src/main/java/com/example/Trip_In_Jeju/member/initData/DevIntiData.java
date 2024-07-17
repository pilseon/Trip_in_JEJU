package com.example.Trip_In_Jeju.member.initData;


import com.example.Trip_In_Jeju.member.servcie.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevIntiData implements BeforeIntiData {
    @Bean
    CommandLineRunner initData(MemberService memberService) {
        return args -> {
            beforeInit();

            // member init
            String password = "1234"; // noop을 해주면 난수처럼 들어감
            memberService.signup2("admin","관리자",password,"admin@test.com","테마");




        };
    }
}
