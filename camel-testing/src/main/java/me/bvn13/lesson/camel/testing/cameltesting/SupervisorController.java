package me.bvn13.lesson.camel.testing.cameltesting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/supervisor")
public class SupervisorController {

    @PostMapping
    public ResponseEntity<SupervisorResponseDto> supervisor(@RequestHeader(name = "file-name") String filename) {

        if (filename.contains("skip")) {
            log.warn("SUPERVISOR / Skipping file: {}", filename);
            return ResponseEntity.ok(new SupervisorResponseDto(SupervisorResponseDto.Verdict.SKIP));
        } else {
            log.warn("SUPERVISOR / Processing file: {}", filename);
            return ResponseEntity.ok(new SupervisorResponseDto(SupervisorResponseDto.Verdict.PROCESS));
        }

    }

}
