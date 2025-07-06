package ktlibrary.infra.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class CategoryAiClient {
    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String inferCategory(String summary) {
        String prompt = String.format(
            "다음은 한국 소설의 줄거리 요약입니다:\n\n" +
            "%s\n\n" +
            "이 소설의 주요 장르 또는 카테고리를 하나 또는 두 개 추천해주세요. " +
            "아래 중에서 가장 적절한 것을 골라주세요:\n\n" +
            "- 로맨스\n" +
            "- 판타지\n" +
            "- SF\n" +
            "- 스릴러\n" +
            "- 추리\n" +
            "- 성장\n" +
            "- 드라마\n" +
            "- 힐링\n" +
            "- 역사\n" +
            "- 사회\n\n" +
            "응답은 쉼표 없이 한글로 하나 또는 두 개만 말해주세요.", summary
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", List.of(message),
                "temperature", 0.2
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", entity, Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
            return (String) messageMap.get("content");
        } else {
            return "카테고리 분류 실패: " + response.getStatusCode();
        }
    }
}
