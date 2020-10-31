package com.example.demooauth2.responseModel;

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
    private Object data;
    private String debugMessage;
    private List<ApiSubErrorViewModel> subErrors;

    public CommandResult() {
        timestamp = LocalDateTime.now();
        data = null;
    }

    public CommandResult(HttpStatus status) {
        this();
        this.status = status;
    }

    public CommandResult(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public CommandResult(HttpStatus status, Object data) {
        this();
        this.status = status;
        this.data = data;
    }

    public CommandResult Succeed() {
        return new CommandResult(HttpStatus.OK);
    }

    public CommandResult SucceedWithData(Object data) {
        return new CommandResult(HttpStatus.OK, data);
    }
}
