package com.example.Trip_In_Jeju.member.initData;


import com.example.Trip_In_Jeju.member.entity.MemberRole;
import com.example.Trip_In_Jeju.member.servcie.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod","dev"})
public class DevIntiData implements BeforeIntiData {

    @Bean(name = "memberInit")
    CommandLineRunner initData(MemberService memberService) {
        return args -> {
            beforeInit();

            // member init
            String password = "1234"; // noop을 해주면 난수처럼 들어감

            // 관리자 계정이 이미 존재하는지 확인
            if (memberService.findByUsername("admin").isEmpty()) {
                memberService.signup2("admin", "관리자", password, "admin@test.com", "테마", MemberRole.ADMIN);
            }

            if (memberService.findByUsername("user1").isEmpty()) {
                memberService.signup2("user1", "필써니", password, "user1@test.com", "관광지", MemberRole.MEMBER);
            }

            if (memberService.findByUsername("user2").isEmpty()) {
                memberService.signup2("user2", "송햄지", password, "user2@test.com", "축제", MemberRole.MEMBER);
            }

            if (memberService.findByUsername("user3").isEmpty()) {
                memberService.signup2("user3", "근혀기", password, "user3@test.com", "음식점", MemberRole.MEMBER);
            }
        };
    }
}