package me.bvn13.lesson.camel.testing.cameltesting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SupervisorResponseDto {

    Verdict verdict;

//    @JsonCreator
//    public SupervisorResponseDto(@JsonProperty("verdict") Verdict verdict) {
//        this.verdict = verdict;
//    }

    public enum Verdict {
        PROCESS,
        SKIP
    }

}
