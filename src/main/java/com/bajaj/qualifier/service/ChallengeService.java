package com.bajaj.qualifier.service;

import com.bajaj.qualifier.constant.ChallengeConstants;
import com.bajaj.qualifier.dto.QuerySubmissionRequest;
import com.bajaj.qualifier.dto.WebhookRequest;
import com.bajaj.qualifier.dto.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class ChallengeService {

    private final WebClient webClient;

    @Value("${bajaj.challenge.name:Sakshi Sonpuriya}")
    private String name;

    @Value("${bajaj.challenge.regNo:YOUR_REG_NO}")
    private String regNo;

    @Value("${bajaj.challenge.email:YOUR_EMAIL}")
    private String email;

    @Value("${bajaj.challenge.baseUrl:https://bfhldevapigw.healthrx.co.in}")
    private String baseUrl;

    public ChallengeService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Executes the main Bajaj Finserv Health qualifier workflow.
     */
    public void executeChallenge() {
        log.info("=================================================================");
        log.info("Starting Bajaj Finserv Qualifier Challenge Workflow...");
        log.info("=================================================================");

        if (regNo == null || regNo.isBlank() || "YOUR_REG_NO".equalsIgnoreCase(regNo.trim())) {
            log.error("ABORTING: Registration number (bajaj.challenge.regNo) is not configured!");
            log.error("Please provide your registration number in application.yml or set BAJAJ_REG_NO env var.");
            log.error("=================================================================");
            return;
        }

        if (email == null || email.isBlank() || "YOUR_EMAIL".equalsIgnoreCase(email.trim())) {
            log.error("ABORTING: Email (bajaj.challenge.email) is not configured!");
            log.error("Please provide your email in application.yml or set BAJAJ_EMAIL env var.");
            log.error("=================================================================");
            return;
        }

        // 1. Generate Webhook POST request payload
        WebhookRequest request = WebhookRequest.builder()
                .name(name.trim())
                .regNo(regNo.trim())
                .email(email.trim())
                .build();

        String targetUrl = baseUrl.trim() + ChallengeConstants.GENERATE_WEBHOOK_PATH;
        log.info("Step 1: Requesting Webhook & Access Token from: {}", targetUrl);
        log.info("Payload: {}", request);

        try {
            // Send POST request to generate Webhook URL and Access Token
            WebhookResponse response = webClient.post()
                    .uri(targetUrl)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(WebhookResponse.class)
                    .block(); // Blocking call since we run on application startup

            if (response == null || response.getWebhook() == null || response.getAccessToken() == null) {
                log.error("FAILED: Received null or invalid response from generation endpoint.");
                log.error("=================================================================");
                return;
            }

            log.info("Step 1 SUCCESS! Details retrieved:");
            log.info("-> Returned Webhook URL: {}", response.getWebhook());
            log.info("-> Access Token (first 15 chars): {}", 
                     response.getAccessToken().substring(0, Math.min(15, response.getAccessToken().length())) + "...");

            // 2. Question Selection Logic based on registration number
            boolean even = isRegNoEven(regNo.trim());
            String selectedQuery = even ? ChallengeConstants.QUESTION_2_SQL : ChallengeConstants.QUESTION_1_SQL;

            log.info("=================================================================");
            log.info("QUESTION DETECTION LOGIC");
            log.info("Registration Number: {}", regNo);
            log.info("Extracted last two digits even? -> {}", even);
            log.info("Selected Question: {}", even ? "QUESTION 2 SQL (Even)" : "QUESTION 1 SQL (Odd)");
            log.info("Selected Query:\n{}", selectedQuery);
            log.info("=================================================================");

            // 3. Post solution to returned webhook URL
            QuerySubmissionRequest submission = QuerySubmissionRequest.builder()
                    .finalQuery(selectedQuery)
                    .build();

            log.info("Step 2: Submitting solution to Webhook URL: {}", response.getWebhook());
            log.info("Sending Authorization Header: <accessToken>");

            String submitResponse = webClient.post()
                    .uri(response.getWebhook())
                    .header("Authorization", response.getAccessToken())
                    .bodyValue(submission)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("=================================================================");
            log.info("Step 2 SUCCESS! Solution submitted successfully.");
            log.info("Server Response:\n{}", submitResponse);
            log.info("=================================================================");

        } catch (Exception e) {
            log.error("FATAL: An error occurred during challenge execution!", e);
            log.error("=================================================================");
        }
    }

    /**
     * Determines whether the registration number's last two digits represent an even number.
     * Extracts all digits, takes the last two, and checks modulo 2.
     */
    public boolean isRegNoEven(String regNo) {
        if (regNo == null || regNo.isBlank()) {
            throw new IllegalArgumentException("Registration number cannot be null or blank");
        }
        
        // Remove all non-digits from string
        String digits = regNo.replaceAll("\\D", "");
        
        if (digits.isEmpty()) {
            log.warn("No digits found in registration number '{}'. Defaulting to ODD.", regNo);
            return false;
        }

        int value;
        if (digits.length() >= 2) {
            String lastTwo = digits.substring(digits.length() - 2);
            value = Integer.parseInt(lastTwo);
        } else {
            value = Integer.parseInt(digits);
        }

        return value % 2 == 0;
    }
}
