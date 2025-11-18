import com.space.command.ICommand;
import com.space.command.UpdateIocResolveDependencyStrategyCommand;
import com.space.ioc.IoC;
import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IocTest {


    @Test
    public void IocShouldUpdateResolveDependencyStrategy() {
        final Boolean[] wasCalled = {false};

        IoC.<ICommand>resolve("Update Ioc Resolve Dependency Strategy",
                (BiFunction<String, Object[], Object>) (depName, args) -> {
                    wasCalled[0] = true;
                    return args;
                }).execute();

        IoC.<Object>resolve("SomeDependency");

        assertTrue(wasCalled[0]);
    }

    @Test
    public void IocShouldThrowInvalidCastExceptionIfDependencyResolvesAnotherType() {
        assertThrows(ClassCastException.class, () -> {
            String strategy = IoC.resolve(
                    "Update Ioc Resolve Dependency Strategy",
                    (BiFunction<String, Object[], Object>) (depName, args) -> new Object(),
                    (BiFunction<String, Object[], Object>) (depName, args) -> args
            );
        });
    }

    @Test
    public void IocShouldThrowArgumentExceptionIfDependencyIsNotFound() {
        IoC.strategy = (BiFunction<String, Object[], Object>) (depName, args) -> {
                    if ("Update Ioc Resolve Dependency Strategy".equals(depName)) {

                        return new UpdateIocResolveDependencyStrategyCommand(
                                (BiFunction<String, Object[], Object>) args[0]
                        );
                    } else {
                        throw new IllegalArgumentException("Dependency {dependency} is not found.");
                    }
                };

        assertThrows(
                IllegalArgumentException.class, () -> {
                    IoC.<ICommand>resolve("UnexistingDependency");
                }
        );
    }

}
