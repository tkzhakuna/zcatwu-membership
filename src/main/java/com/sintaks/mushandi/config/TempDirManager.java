package com.sintaks.mushandi.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;

import java.nio.file.Paths;

@Configuration
@Log4j2
public class TempDirManager implements CommandLineRunner {

    @Value("${tempDirectory}")
    private String tempDirectoryPath;

    @Override
    public void run(final String... args) throws Exception {
        log.info("-----Creating temp dir------");
        
        Files.createDirectories(Paths.get(tempDirectoryPath));
        log.info("------Temp directory created-------");
    }
}

