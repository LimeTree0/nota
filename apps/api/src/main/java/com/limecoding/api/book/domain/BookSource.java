package com.limecoding.api.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookSource {
    KAKAO("Kakao"),
    NAVER("Naver"),
    GOOGLE("Google"),
    MANUAL("Manual");

    private final String source;
}
