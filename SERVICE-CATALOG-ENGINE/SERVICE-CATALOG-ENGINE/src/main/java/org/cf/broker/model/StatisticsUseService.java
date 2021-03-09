package org.cf.broker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*@JsonInclude (JsonInclude.Include.NON_NULL)
@JsonNaming ( PropertyNamingStrategy.SnakeCaseStrategy.class)*/
public class StatisticsUseService {

	/*public StatisticsUseService(String searchYear, int createCnt) {
		this.searchYear = searchYear;
		this.createCnt = createCnt;
	}*/

	/*private String startDate;
	
	private String endDate;*/


	/*private String searchMonth;

	private String searchDay;*/

	private String searchYear;

	private int createCnt;
}
