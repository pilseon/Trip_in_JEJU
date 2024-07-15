package com.example.Trip_In_Jeju.member.dto;

import com.example.Trip_In_Jeju.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "ID를 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    private String passwordCheck;





    public Member toEntity(){
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }
}