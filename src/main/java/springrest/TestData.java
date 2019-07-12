package springrest;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({ "file:src/TestData.properties" })
public interface TestData extends Config {
    int emp1ID();
    String emp1Name();
    String emp1Title();
    int emp1Age();
    int emp2ID();
    String emp2Name();
    String emp2Title();
    int emp2Age();
}