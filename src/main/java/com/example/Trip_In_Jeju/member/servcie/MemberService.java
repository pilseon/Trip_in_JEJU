package com.example.Trip_In_Jeju.member.servcie;

import com.example.Trip_In_Jeju.DataNotFoundException;
import com.example.Trip_In_Jeju.email.service.EmailService;
import com.example.Trip_In_Jeju.email.service.VerificationCodeService;
import com.example.Trip_In_Jeju.member.dto.JoinRequest;
import com.example.Trip_In_Jeju.member.entity.Member;
import com.example.Trip_In_Jeju.member.entity.MemberRole;
import com.example.Trip_In_Jeju.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private  final VerificationCodeService verificationCodeService;

    public boolean checkLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public void join(JoinRequest joinRequest) {
        memberRepository.save(joinRequest.toEntity());
    }



    public Member getLoginMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Transactional
    public Member whenSocialLogin(String username, String nickname, String email) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) {
            return opMember.get();
        }

        // 새로운 회원 저장
        return signupSocialUser(username, nickname, email);
    }

    @Transactional
    public Member signupSocialUser(String username, String nickname, String email) {
        // 소셜 로그인한 회원 저장
        return signup(username, nickname, "", email,null,null,MemberRole.ADMIN);

    }

    @Value("${custom.fileDirPath}")
    private String genFileDirPath;

    @Transactional
    public Member signup(String username, String nickname, String password,
                         String email, String thema, MultipartFile thumbnail, MemberRole role) {

        String thumbnailRelPath = null;

        if (thumbnail != null && !thumbnail.isEmpty()) {
            // 저장할 파일의 경로 및 이름 설정
                thumbnailRelPath = "member/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

            // 디렉토리가 존재하지 않으면 생성
            thumbnailFile.getParentFile().mkdirs();

            try {
                thumbnail.transferTo(thumbnailFile);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .thema(thema)
                .thumbnailImg(thumbnailRelPath)  // 파일 경로 저장
                .role(role)  // 전달된 role 사용
                .createDate(LocalDateTime.now())
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public Member modify(Member member, String nickname, String password,
                         String email, String thema, MultipartFile thumbnail) {
        // 비밀번호가 비어있는지 확인하여 기존 비밀번호를 유지합니다.
        if (password == null || password.isEmpty()) {
            password = member.getPassword(); // 기존 비밀번호를 사용
        } else {
            password = passwordEncoder.encode(password); // 새 비밀번호 암호화
        }

        // 썸네일 파일이 있는 경우, 새 썸네일을 저장합니다.
        String thumbnailRelPath = member.getThumbnailImg(); // 기존 썸네일 경로 유지
        if (thumbnail != null && !thumbnail.isEmpty()) {
            thumbnailRelPath = saveThumbnail(thumbnail); // 새 썸네일 저장
        }

        // 회원 정보를 업데이트합니다.
        member.setNickname(nickname);
        member.setPassword(password);
        member.setEmail(email);
        member.setThema(thema);
        member.setThumbnailImg(thumbnailRelPath); // 썸네일 경로 업데이트
        member.setModifyDate(LocalDateTime.now()); // 수정 날짜 업데이트

        // 수정된 회원 정보를 저장하고 반환합니다.
        return memberRepository.save(member);
    }

    @Transactional
    public Member signup2(String username, String nickname, String password,
                          String email, String thema, MemberRole role ) {

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createDate(LocalDateTime.now())
                .thema(thema)
                .role(role)
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public Member signup3(String username, String nickname, String password,
                          String email, String thema ) {

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createDate(LocalDateTime.now())
                .thema(thema)

                .build();

        return memberRepository.save(member);
    }


    public Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return memberRepository.findByUsername(username).orElse(null);
            }
        }

        return null;
    }


    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElse(null);
    }

    // 썸네일 저장
    private String saveThumbnail(MultipartFile thumbnail) {
        String thumbnailRelPath = "post/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(genFileDirPath + "/" + thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return thumbnailRelPath;
    }


//    public boolean isAdmin(Member member) {
//        return member.isAdmin();
//    }


    // 모든 회원 정보 가져오기
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }



    public boolean isAdmin(Member member) {
        return member.getUsername().equals("admin");
    }

//    @Transactional
//    public void deleteMemberByAdmin(String username) {
//        Member member = memberRepository.findByUsername(username)
//                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + username));
//        deleteRelatedFiles(member.getThumbnailImg());
//        memberRepository.delete(member);
//    }




    // 이메일로 아이디 찾기
    public String findUsernameByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            return member.getUsername();
        }
        throw new DataNotFoundException("해당 이메일로 등록된 사용자가 없습니다."); // 사용자가 없을 경우 예외 발생
    }

    public void sendPasswordResetEmail(String username, String email) {
        // 사용자 확인
        Member member = memberRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 일치하지 않습니다."));

        // 인증코드 생성 및 이메일 전송
        String verificationCode = verificationCodeService.generateVerificationCode(email);
        String subject = "비밀번호 재설정 인증코드";
        String body = "비밀번호 재설정을 위한 인증코드: " + verificationCode;
        emailService.send(email, subject, body);

        // 회원 객체에 인증코드 저장
        member.setResetToken(verificationCode);
        memberRepository.save(member);
    }

    // 인증코드를 사용하여 비밀번호 재설정
    @Transactional
    public void resetPassword(String username, String email, String newPassword) {
        // 사용자 확인
        Member member = memberRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 일치하지 않습니다."));

        // 비밀번호 변경
        member.setPassword(passwordEncoder.encode(newPassword));
        member.setResetToken(null); // 인증코드 초기화
        memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public void deleteMemberByAdmin(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + username));
        deleteRelatedFiles(member.getThumbnailImg());
        memberRepository.delete(member);
    }

    @Transactional
    public void deleteMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + username));


        deleteRelatedFiles(member.getThumbnailImg());


        memberRepository.delete(member);
    }

    private void deleteRelatedFiles(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(genFileDirPath + "/" + filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("파일 삭제 성공: " + filePath);
                } else {
                    System.out.println("파일 삭제 실패: " + filePath);
                }
            }
        }
    }

    public Member getMemberByNickname(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }


    public String getCurrentMemberId() {
        // 현재 로그인된 사용자의 정보를 가져와서 memberId를 반환
        // 예: SecurityContextHolder에서 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal(); // 또는 다른 방식으로 memberId를 추출
    }


}