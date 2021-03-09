package org.cf.broker.model3;

import lombok.Data;

import java.io.Serializable;

@Data
public class JpaVwLngPK implements Serializable {
    private String clntId;
    private String crtDt;
}
