package com.zuovx.Model;
// `schedule_id` int(11) NOT NULL AUTO_INCREMENT,
//         `doctor_id` int(11) NOT NULL,
//         `remainder` int(3) default 40 NOT NULL,
//        `work_time_start` datetime NOT NULL,
//        `is_cancle` boolean default false NOT NULL,
public class Schedule {
    private int scheduleId;
    private int doctorId;
    private int remainder;
    private long workTimeStart;
    private boolean isCancle;

}
