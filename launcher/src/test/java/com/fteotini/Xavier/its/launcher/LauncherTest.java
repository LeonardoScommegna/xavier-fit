package com.fteotini.Xavier.its.launcher;

import com.fteotini.Xavier.commons.tester.ExecutionSummary.ExecutionResult;
import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestEntity;
import com.fteotini.Xavier.commons.tester.ExecutionSummary.TestEntityType;
import com.fteotini.Xavier.commons.tester.MethodUnderTest;
import com.fteotini.Xavier.its.launcher.DONTRUN.DummyTest;
import com.fteotini.Xavier.launcher.MinionProcessBuilder;
import com.fteotini.Xavier.launcher.minion.MinionArgs;
import com.fteotini.Xavier.mutator.IMutationTarget;
import com.fteotini.Xavier.mutator.IMutator;
import com.fteotini.Xavier.mutator.Operators.Base.Operator;
import net.bytebuddy.ByteBuddy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTest")
class LauncherTest {
    private IMutator mutator;

    @BeforeEach
    void setUp() {
        mutator = new DummyMutator();
    }

    @Test
    void it_can_launch_a_single_test_in_a_forked_jvm() throws NoSuchMethodException, IOException, ExecutionException, InterruptedException {
        var args = MinionArgs.ForSingleMethod(new MethodUnderTest(DummyTest.class, DummyTest.class.getDeclaredMethod("it_is_green")), mutator);

        var minionResult = new MinionProcessBuilder(args)
                .start()
                .get();

        assertThat(minionResult.getProcessResult().getExitValue()).isEqualTo(0);

        var classesUnderTest = minionResult.getMutationResults().get(0).getTestContainers();
        assertThat(classesUnderTest)
                .hasSize(1)
                .hasOnlyOneElementSatisfying(c -> assertThat(c.getEntityName()).isEqualTo(DummyTest.class.getSimpleName()));

        var methodUnderTests = classesUnderTest.iterator().next().getChildren();
        assertThat(methodUnderTests)
                .hasSize(1)
                .hasOnlyOneElementSatisfying(t -> {
                    assertThat(t.getEntityName()).isEqualTo("it_is_green()");
                    assertThat(t.getResult()).isEqualTo(ExecutionResult.Success);
                    assertThat(t.getType()).isEqualTo(TestEntityType.Method);
                });
    }

    @Test
    void it_can_run_a_suite_in_a_forked_jvm() throws IOException, ExecutionException, InterruptedException {
        var args = MinionArgs.ForEntireSuite(null, Set.of("com.fteotini.Xavier.its.launcher.DONTRUN"), mutator);
        var minionResult = new MinionProcessBuilder(args)
                .start()
                .get();

        assertThat(minionResult.getProcessResult().getExitValue()).isEqualTo(0);

        var classesUnderTest = minionResult.getMutationResults().get(0).getTestContainers();
        assertThat(classesUnderTest)
                .hasSize(2)
                .extracting(TestEntity::getEntityName).containsExactlyInAnyOrder("DummyTest", "Dummy2Test");

        var dummyTest = getTestEntityByName(classesUnderTest, "DummyTest");
        assertThat(dummyTest.map(TestEntity::getChildren).get())
                .hasSize(1)
                .hasOnlyOneElementSatisfying(t -> {
                    assertThat(t.getEntityName()).isEqualTo("it_is_green()");
                    assertThat(t.getResult()).isEqualTo(ExecutionResult.Success);
                    assertThat(t.getType()).isEqualTo(TestEntityType.Method);
                });

        var dummy2Test = getTestEntityByName(classesUnderTest, "Dummy2Test");
        assertThat(dummy2Test.map(TestEntity::getChildren).get())
                .hasSize(2)
                .anySatisfy(t -> {
                    assertThat(t.getEntityName()).isEqualTo("it_passes()");
                    assertThat(t.getResult()).isEqualTo(ExecutionResult.Success);
                })
                .anySatisfy(t -> {
                    assertThat(t.getEntityName()).isEqualTo("It_Fails()");
                    assertThat(t.getResult()).isEqualTo(ExecutionResult.Failure);
                    assertThat(t.getException()).isNotEmpty();
                });
    }

    private Optional<TestEntity> getTestEntityByName(Set<TestEntity> testEntities, String entityName) {
        return testEntities.stream().filter(e -> e.getEntityName().equals(entityName)).findFirst();
    }

    private static class DummyMutator implements IMutator {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public IMutationTarget getMutationDetails() {
            return null;
        }

        @Override
        public String getUniqueMutationOperationId() {
            return null;
        }

        @Override
        public Operator makeOperator(ByteBuddy buddy) {
            return new Operator() {
                @Override
                public void runMutation(IMutationTarget mutation) {

                }

                @Override
                public void close() throws IOException {

                }
            };
        }
    }
}
