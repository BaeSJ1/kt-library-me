package ktlibrary.infra.application.service;

import ktlibrary.infra.client.CoverAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoverService {
    private final CoverAiClient coverAiClient;

    public String generateCoverImage(String title, String summary) {
        return coverAiClient.generateCoverImage(title, summary);
    }
}
