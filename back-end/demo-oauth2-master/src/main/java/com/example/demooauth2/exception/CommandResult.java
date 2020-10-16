package com.example.demooauth2.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommandResult {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubErrorViewModel> subErrors;

    public CommandResult() {
        timestamp = LocalDateTime.now();
    }

    public CommandResult(HttpStatus status) {
        this();
        this.status = status;
    }

    public CommandResult(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public CommandResult(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public CommandResult Succeed() {
        return new CommandResult(HttpStatus.OK);
    }

    public CommandResult SucceedWithData(String message) {
        return new CommandResult(HttpStatus.OK, message);
    }
}
