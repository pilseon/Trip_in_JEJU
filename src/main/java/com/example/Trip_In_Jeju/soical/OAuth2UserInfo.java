package com.example.Trip_In_Jeju.soical;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();

    String getNickname();
}
