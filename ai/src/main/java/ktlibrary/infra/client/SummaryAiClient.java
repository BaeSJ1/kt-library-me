package ktlibrary.infra.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * SummaryAiClient
 *
 * OpenAI의 Chat Completions API (GPT-4o 모델)를 활용하여 주어진 텍스트(chunk)를 요약하는 컴포넌트입니다.
 * 책 내용을 나눈 청크(chunk)를 받아서, 각 청크에 대해 간결한 요약을 생성합니다.
 * 생성된 요약을 한번 더 요약하여 압축합니다.
 */
@Component
public class SummaryAiClient {

    /**
     * application.yml 또는 application-prod.yml에 정의된 OpenAI API 키를 주입받습니다.
     */
    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 주어진 텍스트 청크(chunk)를 OpenAI GPT-4o API를 사용하여 요약합니다.
     */
    public String summarizeChunk(String manuscriptTitle, String authorName, String introduction, String chunk) {
        String url = "https://api.openai.com/v1/chat/completions";

        // 1. HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 프롬프트 구성
        String prompt = String.format(
        "당신은 한국 출판사의 편집자입니다. 지금부터 작가의 신작 소설에 대한 보도자료나 책 소개문을 작성해야 합니다.\n\n" +

        "아래는 책의 정보입니다:\n\n" +

        "- 제목: %s\n" +
        "- 작가 이름: %s\n" +
        "- 작가 소개: %s\n" +
        "- 책 내용 일부: %s\n\n" +

        "위 내용을 바탕으로 책 소개문을 작성해주세요.\n\n" +

        "스타일:\n" +
        "- 감성적, 문학적\n" +
        "- 등장인물과 분위기 강조\n" +
        "- 5~7문장 정도로 자연스럽게 요약", manuscriptTitle, authorName, introduction, chunk
        );

        return callGpt(prompt, headers, url);
    }

    /**
     * partial summary 들을 통합해 다시 요약하는 메서드
     */
    public String summarizeFinal(String mergedSummaries) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = String.format(
        "아래는 책의 각 부분을 요약한 내용입니다. 이 요약들을 기반으로 최종적으로 하나의 소개 문장을 작성해주세요.\n\n" + 
        
        "작성 방식:\n" +
        "- 감성적이고 문학적인 어조\n" +
        "- 전체 분위기, 등장인물, 주제 등을 포괄\n" +
        "- 독자에게 소개하는 듯한 느낌\n" +
        "- 10문장 이내\n\n" +

        "내용:\n" +
        "%s", mergedSummaries
        );

        return callGpt(prompt, headers, url);
    }

    private String callGpt(String prompt, HttpHeaders headers, String url) {
        // 메시지 구성
        Map<String, Object> message = Map.of(
            "role", "user",
            "content", prompt
        );

        // 요청 본문 구성
        Map<String, Object> body = Map.of(
            "model", "gpt-4o",
            "messages", List.of(message),
            "temperature", 0.5
        );

        // HTTP 요청 전송
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // 응답 파싱 및 반환
        if (response.getStatusCode() == HttpStatus.OK) {
            // GPT 응답에서 "choices" → [0] → "message" → "content" 추출
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
            return (String) messageMap.get("content");
        } else {
            // 에러 상태 코드 반환
            return "요약 생성 실패: " + response.getStatusCode();
        }
    }
}