package ktlibrary.infra.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    @Value("${file.base-url}")
    private String baseUrl;

    @Value("${file.storage-path}")
    private String storagePath;

    public String savePdf(Long bookId, byte[] fileData) throws IOException {
        String fileName = storagePath + "/" + bookId + ".pdf";
        Path path = Paths.get(fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, fileData);

        return baseUrl + "/" + fileName;
    }
}
