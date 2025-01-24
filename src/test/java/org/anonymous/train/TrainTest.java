package org.anonymous.train;

import org.anonymous.train.entities.TrainData;
import org.anonymous.train.repositories.TrainDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles({"default", "ml"})
public class TrainTest {

    @Autowired
    private TrainDataRepository repository;

    @Test
    void test1() {

        List<TrainData> items = new ArrayList<>();

        TrainData item1 = TrainData.builder()
                .item1(1000)
                .item2(2000)
                .target(0)
                .build();

        items.add(item1);

        TrainData item2 = TrainData.builder()
                .item1(2000)
                .item2(3000)
                .target(1)
                .build();

        items.add(item2);

        TrainData item3 = TrainData.builder()
                .item1(3000)
                .item2(4000)
                .target(2)
                .build();

        items.add(item3);

        TrainData item4 = TrainData.builder()
                .item1(4000)
                .item2(5000)
                .target(3)
                .build();

        items.add(item4);
    }
}