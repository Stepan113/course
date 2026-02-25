package org.course.courserpp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReportRow {
    private String accountNumber;
    private String accountBalance;
    private String type;
    private Long amount;
    private Date date;
    private String applicationId;
}
