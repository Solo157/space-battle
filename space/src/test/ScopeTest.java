import com.space.command.ICommand;
import com.space.command.InitCommand;
import com.space.ioc.IoC;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScopeTest {

    public void setUp()
    {
        new InitCommand().execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();
    }

    @Test
    public void Ioc_Should_Resolve_Registered_Dependency_In_CurrentScope() {
        setUp();

        IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)1).execute();

        assertEquals(1, IoC.<Integer>resolve("someDependency"));

        dispose();
    }

    @Test
    public void Ioc_Should_Throw_Exception_On_Unregistered_Dependency_In_CurrentScope() {
        setUp();

        assertThrows(
                RuntimeException.class, () -> {
                    IoC.<Integer>resolve("someDependency");
                }
        );

        dispose();
    }

    @Test
    public void Ioc_Should_Use_Parent_Scope_If_Resolving_Dependency_Is_Not_Defined_In_Current_Scope() {
        setUp();

        IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)1).execute();

        var parentIoCScope = IoC.<Object>resolve("IoC.Scope.Current");

        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();

        assertEquals(iocScope, IoC.<Object>resolve("IoC.Scope.Current"));
        assertEquals(1, IoC.<Integer>resolve("someDependency"));

        dispose();
    }

    @Test
    public void Paret_Scope_Can_Be_Set_Manually_For_Creating_Scope() {
        setUp();

        var scope1 = IoC.<Object>resolve("IoC.Scope.Create");
        var scope2 = IoC.<Object>resolve("IoC.Scope.Create", scope1);

        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope1);
        IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)2).execute();
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope2);

        assertEquals(2, IoC.<Integer>resolve("someDependency"));

        dispose();
    }

    /**
     * Тест, проверяющий, что в IoC контейнере может быть одна зависимость, но в зависимости от текущего установленного
     * скоупа реализация этой зависимости будет разная.
     */
    @Test
    public void SeparateScopes_ShouldMaintainIndependentRegistrations() {
        setUp();

        var scope1 = IoC.<Object>resolve("IoC.Scope.Create");
        var scope2 = IoC.<Object>resolve("IoC.Scope.Create");

        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope1).execute();
        IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)1).execute();

        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope2).execute();
        IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)2).execute();

        // assert
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope1).execute();
        assertEquals(1, IoC.<Integer>resolve("someDependency"));
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope2).execute();
        assertEquals(2, IoC.<Integer>resolve("someDependency"));

        dispose();
    }

    /**
     * Тест, проверяющий использование одного скоупа разными потоками. Убеждаемся, что хоть скоуп один, для каждого
     * потока он будет разным.
     */
    @Test
    public void ConcurrentScopeRegistrations_ShouldMaintainIsolation() {
        setUp();

        var scope1 = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", scope1).execute();

        new Thread(() -> {
            IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)1).execute();
            assertEquals(1, IoC.<Integer>resolve("someDependency"));
        }).start();

        new Thread(() -> {
            IoC.<ICommand>resolve("IoC.Register", "someDependency", (Function<Object[], Object>) (args) -> (Object)2).execute();
            assertEquals(2, IoC.<Integer>resolve("someDependency"));
        }).start();

        dispose();
    }

    public void dispose()
    {
        IoC.<ICommand>resolve("IoC.Scope.Current.Clear").execute();
    }

}
