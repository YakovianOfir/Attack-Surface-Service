package com.orca.spring.breacher.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ControllerErrorResponse
{
    // region Fields

    @Getter @Setter
    private HttpStatus status;

    @Getter @Setter
    private String errorCode;

    @Getter @Setter
    private String errorMessage;

    @Getter @Setter
    private String furtherDetails;

    @Getter @Setter
    private LocalDateTime timeStamp;

    // endregion

    public static final class ControllerErrorResponseBuilder
    {
        // region Fields
        
        private HttpStatus status;
        private String errorCode;
        private String errorMessage;
        private String furtherDetails;
        private LocalDateTime timeStamp;
        
        // endregion

        public ControllerErrorResponseBuilder()
        {}

        public ControllerErrorResponseBuilder withStatus(HttpStatus status)
        {
            this.status = status;
            return this;
        }

        public ControllerErrorResponseBuilder withErrorCode(String errorCode)
        {
            this.errorCode = errorCode;
            return this;
        }

        public ControllerErrorResponseBuilder withErrorMessage(String message)
        {
            this.errorMessage = message;
            return this;
        }

        public ControllerErrorResponseBuilder withFurtherDetails(String furtherDetails)
        {
            this.furtherDetails = furtherDetails;
            return this;
        }

        public ControllerErrorResponseBuilder atTime(LocalDateTime timeStamp)
        {
            this.timeStamp = timeStamp;
            return this;
        }

        public ControllerErrorResponse build()
        {
            var errorResponse = new ControllerErrorResponse();
            errorResponse.furtherDetails = this.furtherDetails;
            errorResponse.errorMessage = this.errorMessage;
            errorResponse.timeStamp = this.timeStamp;
            errorResponse.errorCode = this.errorCode;
            errorResponse.status = this.status;
            return errorResponse;
        }
    }
}