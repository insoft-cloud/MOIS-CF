package org.cf.broker.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {
    @JsonProperty("description")
    private String message;

    @JsonProperty("status")
    private String status;

    @JsonProperty("code")
    private int code;

}
