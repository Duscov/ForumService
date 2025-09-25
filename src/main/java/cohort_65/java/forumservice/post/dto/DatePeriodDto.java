package cohort_65.java.forumservice.post.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DatePeriodDto {
    LocalDate dateFrom;
    LocalDate dateTo;
}