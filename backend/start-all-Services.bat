@echo off
cd /d %~dp0

echo Starting Eureka Server...
start cmd /k java -jar Eureka-Server\target\Eureka-Server-0.0.1-SNAPSHOT.jar

echo Waiting 10 seconds for Eureka to initialize...
timeout /t 10 /nobreak > nul

echo Starting Config Server...
start cmd /k java -jar configServer\target\configServer-0.0.1-SNAPSHOT.jar

echo Waiting 10 seconds for Config to initialize...
timeout /t 10 /nobreak > nul

echo Starting API Gateway...
start cmd /k java -jar Api-Gateway\target\Api-Gateway-0.0.1-SNAPSHOT.jar

echo Starting User Service...
start cmd /k java -jar UserService\target\UserService-0.0.1-SNAPSHOT.jar

echo Starting Product Service...
start cmd /k java -jar ProductService\target\ProductService-0.0.1-SNAPSHOT.jar

echo Starting Order Service...
start cmd /k java -jar OrderService\target\OrderService-0.0.1-SNAPSHOT.jar

echo Starting Billing Service...
start cmd /k java -jar BillingService\target\BillingService-0.0.1-SNAPSHOT.jar

echo Starting Notification Service...
start cmd /k java -jar NotificationService\target\NotificationService-0.0.1-SNAPSHOT.jar

echo All services launched 