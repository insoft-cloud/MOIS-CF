package org.cf.servicebroker.model.serviceinstance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.servicebroker.common.Constants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceInstanceLastOperation {

    private String description;

    @JsonIgnore
    private OperationState state;

    private String error;

    @JsonProperty("state")
    public String getState() {
        switch(this.state) {
            case IN_PROGRESS:
                return Constants.IN_PROGRESS;
            case SUCCEEDED:
                return Constants.SUCCEEDED;
            case FAILED:
                return "failed";
            default:
                assert false;
                return "internal error";
        }
    }


    public void setState(int state) {
        switch(state) {
            case 0:
                this.state = OperationState.IN_PROGRESS;
                break;
            case 1:
                this.state = OperationState.SUCCEEDED;
                break;
            case 2:
                this.state = OperationState.FAILED;
                break;
            default:
                assert false;
        }

    }
}
