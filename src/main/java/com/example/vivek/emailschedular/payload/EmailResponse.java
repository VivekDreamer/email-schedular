package com.example.vivek.emailschedular.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class EmailResponse {
    @NonNull
    private boolean success;
    private String jobId;
    private String jobGroup;
    @NonNull
    private String message;
}
