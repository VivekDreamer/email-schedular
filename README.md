
## How to setup the project?

**1. Clone the repository** 
```bash
  git clone https://github.com/VivekDreamer/email-schedular
```
**2. Create MySQL database**
```bash
  create database quartz_demo 
```
**3. Setup the application.properties file by giving MySQL configuration**
```bash
  spring.datasource.url=
  spring.datasource.username=
  spring.datasource.password=
```
**4. Create the quartz table in database**
```bash
  We scheduled Jobs in MySQL database. You'll need to create the tables that 
  Quartz uses to store Jobs and other job-related data. Please create Quartz 
  specific tables by executing the quartz_tables.sql script located inside 
  src/main/resources directory.

  mysql> source <PATH_TO_QUARTZ_TABLES.sql>
```
**5. Setup spring mail properties**
```bash
  Gmail's SMTP server is used for sending emails in this project. Whatever SMTP
  server you are using, you'll need to configure the following mail properties
  accordingly :
  spring.mail.host=smtp.gmail.com
  spring.mail.port=587
  spring.mail.username=
  spring.mail.password=

  If you're using Gmail, you need to allow the third party apps to send emails
  by generating app password.
```

**6. Build and run the app using maven**
```bash
  mvn spring-boot:run
```

**7. /scheduleEmail API**
```bash
  curl --location 'http://localhost:8080/schedule/email' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "vmakv1998@gmail.com",
    "subject": "testing",
    "body": "testing email",
    "dateTime": "2023-09-09T15:42:00",
    "timeZone": "Asia/Kolkata"
}'

Output

{
    "success": true,
    "jobId": "a4804ef6-3104-4594-a146-df2d6dde608d",
    "jobGroup": "email-jobs",
    "message": "Email scheduled successfully!!"
}
```
