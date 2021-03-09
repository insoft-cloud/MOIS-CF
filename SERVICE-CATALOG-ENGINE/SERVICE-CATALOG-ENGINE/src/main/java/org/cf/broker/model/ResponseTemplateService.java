package org.cf.broker.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cf.broker.model.jpa.JpaService;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseTemplateService {
    String serviceId;
    String serviceNm;

    public ResponseTemplateService(JpaService jpaService){
        this.serviceId = jpaService.getId();
        this.serviceNm = jpaService.getSvcNm();
    }
}
