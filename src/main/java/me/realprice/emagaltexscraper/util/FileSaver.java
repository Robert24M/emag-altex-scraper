package me.realprice.emagaltexscraper.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@Component
public class FileSaver {

    @Value("${app.output.dir}")
    private String outputDir;

    public void saveFile(String fileName, String content) {
        try {
            Path filePath = Path.of(outputDir, fileName);

            Files.createDirectories(filePath.getParent());

            Files.write(filePath, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
