package org.cf.broker.model.jpa;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service_statistics")
@Builder
@JsonNaming ( PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JpaStatisticsService {

	/*public StatisticsUseService(String searchYear, int createCnt) {
		this.searchYear = searchYear;
		this.createCnt = createCnt;
	}*/

	/*private String startDate;
	
	private String endDate;*/


	/*private String searchMonth;

	private String searchDay;*/

	@Id
	@Column (name = "search_year")
	private String searchDate;

	@Column (name = "create_cnt")
	private int createCnt;
}
