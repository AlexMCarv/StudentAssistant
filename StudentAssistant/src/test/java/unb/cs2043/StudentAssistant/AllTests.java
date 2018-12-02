package unb.cs2043.StudentAssistant;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AlgorithmV1MethodsTest.class, AppTest.class, ClassTimeTest.class, ConflictsWithTest.class,
		EqualsTest.class, ScheduleArrangerTest.class, ScheduleSubsetTest.class, SectionSameClassTimeTest.class })
public class AllTests {

}
