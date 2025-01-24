package org.anonymous.train.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.train.entities.QTrainData;
import org.anonymous.train.entities.TrainData;
import org.anonymous.train.repositories.TrainDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Slf4j
@Service
@Profile("ml")
@RequiredArgsConstructor
public class TrainService {

    @Value("${python.path}")
    private String runPath;

    @Value("${python.script}")
    private String scriptPath;

    private final TrainDataRepository repository;

    /**
     * 훈련 데이터 목록 조회
     *
     * @param isAll
     * @return
     */
    public List<TrainData> getList(boolean isAll) {

        if (isAll) {

            return repository.findAll();

        } else {

            QTrainData trainData = QTrainData.trainData;

            return (List<TrainData>) repository.findAll(trainData.done.eq(false));
        }
    }

    // 매일 자정 훈련 진행
    @Scheduled(cron = "0 0 0 * * *")
    public void train() {

        try {
            log.info("훈련 시작");

            ProcessBuilder builder = new ProcessBuilder(runPath, scriptPath + "partial.py");

            Process process = builder.start();

            int code = process.waitFor();

            log.info("훈련 완료 : {}", code);

            QTrainData trainData = QTrainData.trainData;

            // 훈련 데이터 완료 처리
            List<TrainData> items = repository.findAll(trainData.done.eq(false));

            items.forEach(item -> item.setDone(true));

            repository.saveAllAndFlush(items);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}