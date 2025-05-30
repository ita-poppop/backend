package com.example.poppop.domain.popup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopupRedisService {
    //Redis에 값 저장/조회/증가/TTL 관리 등 저수준 로직만 담당
    private final RedisTemplate<String, String> redisTemplate;

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    // viewCount에서는 안쓰임
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    // ex 기존에 멤버가 본 팝업에서 새로운 팝업 추가
    public void appendValues(String key, String value) {
        redisTemplate.opsForValue().append(key, value);
    }
    public void setDateExpire(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }
    public void addViewCountInRedis(String popupId) {
        String key = "popup:"+popupId;
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        // 이미 값이 있으면 덮어쓰지 않고, 없으면 0으로 시작
        if (valueOps.get(key) == null) {
            valueOps.set(key, "0");
        } else {
            valueOps.increment(key);
        }
        setOps.add("PopupKeyList",popupId);
    }
    // PopupKeyList에서 모든 게시글ID를 pop(꺼내고 삭제)
    public List<String> deleteKeyList() {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        List<String> value = setOps.pop("PopupKeyList",setOps.size("PopupKeyList"));
        return value;
    }
    // popup:popupId 형태의 키값에서 viewCount 가져오고 삭제
    public String getAndDeleteViewCount(String key) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key);
    }
}
