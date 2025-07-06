package ktlibrary.infra.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class CoverAiClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateCoverImage(String title, String summary) {
        String prompt = String.format(
            "Book cover illustration for the Korean novel titled \"%s\".\n" +
            "The story is: %s\n" +
            "Style: Soft, dreamy, emotional, illustrated book cover, pastel colors.\n",
            title, summary
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", "dall-e-3",
                "prompt", prompt,
                "n", 1,
                "size", "1024x1024"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/images/generations",
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            return (String) data.get(0).get("url");  // 생성된 이미지 URL 반환
        } else {
            return "이미지 생성 실패: " + response.getStatusCode();
        }
    }
}
