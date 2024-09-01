package com.example.Trip_In_Jeju.soical;

import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.entity.MemberRole;
import com.example.Trip_In_Jeju.member.repository.MemberRepository;
import com.example.Trip_In_Jeju.soical.detail.GoogleUserDetails;
import com.example.Trip_In_Jeju.soical.detail.KakaoUserDetails;
import com.example.Trip_In_Jeju.soical.detail.NaverUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if (provider.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        } else if (provider.equals("naver")) {
            log.info("네이버 로그인");
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String encryptedProviderId = encryptProviderId(providerId);
        String loginId = provider + "_" + encryptedProviderId;
        String username = oAuth2UserInfo.getName();
        String nickname = oAuth2UserInfo.getNickname();

        Member findMember = memberRepository.findByLoginId(loginId);
        Member member;

        if (findMember == null) {
            member = Member.builder()
                    .nickname(nickname)
                    .loginId(loginId)
                    .username(username)
                    .provider(provider)
                    .createDate(LocalDateTime.now())
                    .role(MemberRole.MEMBER)
                    .email(email)
                    .providerId(providerId)
                    .build();
            memberRepository.save(member);
        } else {
            member = findMember;
        }

        String name = (username != null) ? username : "";

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes(), name);
    }

    private String encryptProviderId(String providerId) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(providerId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash); // Base64로 변환
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
