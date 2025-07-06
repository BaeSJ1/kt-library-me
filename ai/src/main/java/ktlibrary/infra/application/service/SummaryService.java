package ktlibrary.infra.application.service;

import ktlibrary.infra.client.SummaryAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SummaryService
 *
 * 책의 전체 내용을 분할(Chunking)하여 생성형 AI를 통해 요약하고,
 * 부분 요약들을 종합하여 최종 요약 결과를 반환하는 서비스 클래스입니다.
 *
 * 생성형 AI 클라이언트(SummaryAiClient)를 통해 실제 요약 API 호출을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryAiClient summaryAiClient;

    /**
     * 주어진 긴 텍스트(책 내용)를 적절한 길이로 나눈 후,
     * 각 청크(chunk)를 요약하고 결과를 결합하여 하나의 요약문으로 반환합니다.
     * 반환된 요약을 다시 한번 요약을 수행하여 최종 요약본을 만듭니다.
     */
    public String summarize(String manuscriptTitle, String manuscriptContent, String authorName, String introduction) {
        // 줄 단위로 청크를 나누고, 각 청크를 요약한 후 결과를 모아 반환
        List<String> chunks = splitByLineChunks(manuscriptContent, 1000, 200);

        // 각 청크를 요약한 내용을 저장할 리스트
        List<String> partialSummaries = new ArrayList<>();

        // partial 요약
        for (String chunk : chunks) {
            String partial = summaryAiClient.summarizeChunk(
                manuscriptTitle, authorName, introduction, chunk
            );
            partialSummaries.add(partial);
        }

        // 계층적 요약: partial 요약을 최종 요약으로 다시 한번 압축
        String joinedSummaries = String.join("\n", partialSummaries);

        // 최종 요약본 반환
        return summaryAiClient.summarizeFinal(joinedSummaries);
    }

    /**
     * 줄 단위로 텍스트를 분할하여 일정 길이의 청크로 나누는 함수입니다.
     * 각 청크 간에는 일부 내용이 겹치도록(overlap) 처리하여 문맥 유실을 방지합니다.
     *
     * @param text         전체 입력 텍스트
     * @param chunkSize    한 청크의 최대 길이(문자 수 기준)
     * @param chunkOverlap 청크 간 중첩(overlap)될 문자 수
     * @return 분할된 청크 리스트
     */
    private List<String> splitByLineChunks(String text, int chunkSize, int chunkOverlap) {
        String[] lines = text.split("\\n");
        List<String> chunks = new ArrayList<>();

        StringBuilder currentChunk = new StringBuilder();
        int currentLength = 0;
        int i = 0;

        while (i < lines.length) {
            // 현재 청크에 라인을 추가하며 최대 길이까지 확장
            while (i < lines.length && currentLength + lines[i].length() < chunkSize) {
                currentChunk.append(lines[i]).append("\n");
                currentLength += lines[i].length();
                i++;
            }
            chunks.add(currentChunk.toString());

            // overlap 영역 확보 (이전 라인 중 일부를 다음 청크에 포함)
            int overlapLength = 0;
            int overlapIndex = i;
            StringBuilder overlapChunk = new StringBuilder();
            while (overlapIndex > 0 && overlapLength < chunkOverlap) {
                overlapIndex--;
                String line = lines[overlapIndex];
                overlapLength += line.length();
                overlapChunk.insert(0, line + "\n");
            }

            // 다음 청크 초기화
            currentChunk = new StringBuilder(overlapChunk);
            currentLength = overlapLength;
        }

        return chunks;
    }
}
