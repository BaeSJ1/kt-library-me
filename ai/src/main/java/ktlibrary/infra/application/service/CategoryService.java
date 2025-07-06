package ktlibrary.infra.application.service;

import ktlibrary.infra.client.CategoryAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryAiClient categoryAiClient;

    public String generateCategory(String summary) {
        return categoryAiClient.inferCategory(summary);
    }
}
