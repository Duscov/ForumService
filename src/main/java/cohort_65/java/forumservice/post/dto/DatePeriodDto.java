package cohort_65.java.forumservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DatePeriodDto {
    private LocalDate dateFrom;
    private LocalDate dateTo;
}