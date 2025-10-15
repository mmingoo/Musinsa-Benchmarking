package com.avengers.musinsa.domain.user.auth.oauth2.dto;

import java.util.Map;
public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    // 추가 정보 추출 메서드들
    public String getGender() {
        return attribute.get("gender") != null ? attribute.get("gender").toString() : null;
    }

    public String getBirthyear() {
        return attribute.get("birthyear") != null ? attribute.get("birthyear").toString() : null;
    }

    public String getBirthday() {
        return attribute.get("birthday") != null ? attribute.get("birthday").toString() : null;
    }

    public String getMobile() {
        return attribute.get("mobile") != null ? attribute.get("mobile").toString() : null;
    }

    public String getAge() {
        return attribute.get("age") != null ? attribute.get("age").toString() : null;
    }

    // 연령대 계산 메서드
    public String getAgeGroup() {
        String birthyear = getBirthyear();
        if (birthyear != null) {
            try {
                int year = Integer.parseInt(birthyear);
                int currentYear = java.time.Year.now().getValue();
                int age = currentYear - year + 1;

                if (age < 20) return "10대";
                else if (age < 30) return "20대";
                else if (age < 40) return "30대";
                else if (age < 50) return "40대";
                else if (age < 60) return "50대";
                else return "60대 이상";
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}