package org.anonymous.train.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Lazy
@Slf4j
@Service
@Profile("ml")
@RequiredArgsConstructor
public class PredictService {

    @Value("${python.path}")
    private String runPath;

    @Value("${python.script}")
    private String scriptPath;

    private final ObjectMapper om;

    public List<Integer> predict(List<Double> items) {

        try {

            String data = om.writeValueAsString(items);

            ProcessBuilder builder = new ProcessBuilder(runPath, scriptPath + "predict.py", data);

            Process process = builder.start();

            InputStream in = process.getInputStream();

            return om.readValue(in.readAllBytes(), new TypeReference<>() {});

        } catch (Exception e) {

            e.printStackTrace();
        }

        return List.of();
    }
}