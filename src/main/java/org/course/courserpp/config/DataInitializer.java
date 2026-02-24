package org.course.courserpp.config;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.*;
import org.course.courserpp.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final ScoreRepository scoreRepository;
    private final CashWithdrawalRepository cashWithdrawalRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    private final ApplicationForCashRepository applicationForCashRepository;
    private final ReplenishmentOperationRepository replenishmentOperationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        // Users
        User guarantor = new User();
        guarantor.setLogin("guarantor");
        guarantor.setPassword(passwordEncoder.encode("password123"));
        guarantor.setRole(Role.GUARANTOR);
        userRepository.save(guarantor);

        User executor = new User();
        executor.setLogin("executor");
        executor.setPassword(passwordEncoder.encode("password123"));
        executor.setRole(Role.EXECUTOR);
        userRepository.save(executor);

        // Cards
        for (int i = 1; i <= 4; i++) {
            Card card = new Card();
            card.setNumber(4000000000000000L + i);
            card.setBalance(100000L * i);
            card.setCVC("12" + i);
            card.setUser(executor);
            cardRepository.save(card);
        }

        // Scores
        Score[] scores = new Score[4];
        for (int i = 1; i <= 4; i++) {
            Score score = new Score();
            score.setNumber("ACC-000" + i);
            score.setBalance(String.valueOf(500000L * i));
            score.setCurrency(810);
            score.setUser(guarantor);
            scores[i - 1] = scoreRepository.save(score);
        }

        // ApplicationForCash
        ApplicationForCash[] apps = new ApplicationForCash[4];
        for (int i = 1; i <= 4; i++) {
            ApplicationForCash app = new ApplicationForCash();
            app.setDelay((long) (i * 7));
            app.setWithdrawalAmount(10000L * i);
            apps[i - 1] = applicationForCashRepository.save(app);
        }

        // CashWithdrawals
        for (int i = 0; i < 4; i++) {
            CashWithdrawal cw = new CashWithdrawal();
            cw.setWithdrawalAmount(5000L * (i + 1));
            cw.setApplicationForCash(apps[i]);
            cashWithdrawalRepository.save(cw);
        }

        // MoneyTransfers
        for (int i = 1; i <= 4; i++) {
            MoneyTransfer mt = new MoneyTransfer();
            mt.setSum(20000L * i);
            moneyTransferRepository.save(mt);
        }

        // ReplenishmentOperations
        for (int i = 1; i <= 4; i++) {
            ReplenishmentOperation ro = new ReplenishmentOperation();
            ro.setSum(15000L * i);
            replenishmentOperationRepository.save(ro);
        }
    }
}
