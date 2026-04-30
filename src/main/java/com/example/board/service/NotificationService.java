package com.example.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String supabaseServiceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void createNotification(String userId, String actorId, String actorName,
                                   Long postId, String postTitle, Long commentId, String message) {
        if (userId == null || userId.equals(actorId)) return; // 본인 댓글 알림 제외

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseServiceKey);
        headers.set("Authorization", "Bearer " + supabaseServiceKey);

        Map<String, Object> body = new HashMap<>();
        body.put("user_id", userId);
        body.put("actor_id", actorId);
        body.put("actor_name", actorName);
        body.put("post_id", postId);
        body.put("post_title", postTitle);
        body.put("comment_id", commentId);
        body.put("message", message);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(supabaseUrl + "/rest/v1/notifications", request, String.class);
    }
}