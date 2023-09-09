package com.example.vivek.emailschedular.controller;

import com.example.vivek.emailschedular.payload.EmailRequest;
import com.example.vivek.emailschedular.payload.EmailResponse;
import com.example.vivek.emailschedular.quartz.job.EmailJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
public class EmailSchedularController {

    @Autowired
    private Scheduler scheduler;

    @PostMapping("/schedule/email")
    public ResponseEntity<EmailResponse> scheduleEmail(@Valid @RequestBody EmailRequest emailRequest){
        try {
            ZonedDateTime dateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())){
                EmailResponse resp = new EmailResponse(false,"date time must be after current time");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(resp);
            }
            JobDetail jobDetail = buildJobDetail(emailRequest);
            Trigger trigger = buildTrigger(jobDetail,dateTime);
            scheduler.scheduleJob(jobDetail, trigger);
            EmailResponse resp = new EmailResponse(true,jobDetail.getKey().getName(),jobDetail.getKey().getGroup(),
                    "Email scheduled successfully!!");
            return ResponseEntity.ok(resp);
        }catch (Exception ex){
            log.error("Error while scheduling email: ", ex);
            EmailResponse resp = new EmailResponse(false,"Error while scheduling email. Please try again later!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(resp);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<String> getApiTest(){
        return ResponseEntity.ok("Get API test - pass");
    }

    private JobDetail buildJobDetail(EmailRequest emailRequest){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email",emailRequest.getEmail());
        jobDataMap.put("body",emailRequest.getBody());
        jobDataMap.put("subject",emailRequest.getSubject());
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(),"email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(jobDataMap)
                .storeDurably().build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(),"email-triggers")
                .withDescription("Send Email Triggers")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

    }
}
