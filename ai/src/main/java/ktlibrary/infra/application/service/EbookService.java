package ktlibrary.infra.application.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.io.InputStream;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EbookService {
    public byte[] generateEbook(String title, String authorName, String content) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 한글 지원 폰트 로드
            String fontPath = "fonts/NanumGothic.ttf"; // resources 아래 상대 경로
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if (fontStream == null) {
                throw new RuntimeException("폰트 파일을 찾을 수 없습니다: " + fontPath);
            }
            PdfFont font = PdfFontFactory.createFont(
                fontStream.readAllBytes(),
                PdfEncodings.IDENTITY_H,
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
            );

            // 폰트 설정 후 Paragraph에 적용
            document.setFont(font);
            document.add(new Paragraph("제목: " + title).setBold().setFontSize(16));
            document.add(new Paragraph("작가: " + authorName).setItalic().setFontSize(12));
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph(content));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("전자책 생성 실패", e);
        }
    }
}
