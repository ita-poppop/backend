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
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
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
        valueOps.increment(key);
        setOps.add("PopupKeyList",popupId);
    }

    public void incrementViewCount(String popupId) {
        String key = "popup:" + popupId;
        redisTemplate.opsForValue().increment(key);
    }

    public void addPopupIdToSet(String popupId) {
        redisTemplate.opsForSet().add("PopupKeyList", popupId);
    }
    public List<String> deleteKeyList() {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        List<String> value = setOps.pop("PopupKeyList",setOps.size("PopupKeyList"));
        return value;
    }
    public String getAndDeleteViewCount(String popupId) {
        String key = "popup:" + popupId;
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String count = valueOps.get(key);
        redisTemplate.delete(key);
        return count;
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
