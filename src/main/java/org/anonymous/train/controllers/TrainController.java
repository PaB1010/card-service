package org.anonymous.train.controllers;

import lombok.RequiredArgsConstructor;
import org.anonymous.train.entities.TrainData;
import org.anonymous.train.services.PredictService;
import org.anonymous.train.services.TrainService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Profile("ml")
@RestController
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    private final PredictService predictService;

    /**
     * 훈련 데이터
     *
     * @param mode
     * @return
     */
    @GetMapping("/train/{mode}")
    public List<TrainData> train(@PathVariable("mode") String mode) {

        return trainService.getList(mode.equals("all"));
    }

    /**
     * 예측 데이터
     *
     * @param items
     * @return
     */
    @GetMapping("/predict")
    public List<Integer> predict(@RequestParam("data") List<Double> items) {

        return predictService.predict(items);
    }
}