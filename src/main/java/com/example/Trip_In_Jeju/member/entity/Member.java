package com.example.Trip_In_Jeju.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    @Comment("유저 아이디")
    private String loginId;

    private String password;

    @Comment("유저 닉네임")
    private String nickname;

    @Column(unique = true)
    private String username;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private String provider;

    private String providerId;

    private String thema;

    @Comment("사진")
    private String thumbnailImg;

    private String email;

    private String googleId;
    private String naverId;

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getNaverId() {
        return naverId;
    }

    public void setNaverId(String naverId) {
        this.naverId = naverId;
    }

    private boolean admin;

    public String getThumbnailImg() {
        return thumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

//    public void getUsername(String username) {
//        this.username = username;
//    }
// lombok 중복으로 에러 날 수 있음
    public void setResetToken(String resetToken) { // 이 부분을 추가합니다.
        this.resetToken = resetToken;
    }

    private String resetToken;


}