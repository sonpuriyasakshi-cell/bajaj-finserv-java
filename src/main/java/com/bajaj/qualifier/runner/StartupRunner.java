package com.bajaj.qualifier.runner;

import com.bajaj.qualifier.service.ChallengeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final ChallengeService challengeService;

    public StartupRunner(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @Override
    public void run(String... args) throws Exception {
        challengeService.executeChallenge();
    }
}
