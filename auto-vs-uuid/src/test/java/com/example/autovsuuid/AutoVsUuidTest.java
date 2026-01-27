package com.example.autovsuuid;

import com.example.autovsuuid.domain.Auto;
import com.example.autovsuuid.domain.Uuid;
import com.example.autovsuuid.repository.AutoReposiory;
import com.example.autovsuuid.repository.UuidRespoitory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
class AutoVsUuidTest {

    @Autowired
    private AutoReposiory autoReposiory;

    @Autowired
    private UuidRespoitory uuidRespoitory;

    private static final int COUNT = 10_000;

    @Test
    void autoIncrement_vs_uuid_insert_속도_비교() {
        List<Auto> autos = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            autos.add(new Auto("auto_" + i));
        }

        List<Uuid> uuids = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            uuids.add(new Uuid("uuid_" + i));
        }

        StopWatch stopWatch = new StopWatch("Auto Increment vs UUID Insert");

        stopWatch.start("Auto Increment (IDENTITY) - " + COUNT + "건");
        autoReposiory.saveAll(autos);
        stopWatch.stop();

        stopWatch.start("UUID (직접 할당) - " + COUNT + "건");
        uuidRespoitory.saveAll(uuids);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
