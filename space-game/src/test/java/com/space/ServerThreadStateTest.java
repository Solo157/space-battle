package com.space;

import com.space.command.*;
import com.space.command.states.MoveToState;
import com.space.event.ManualResetEvent;
import com.space.serverthread.ServerThread;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестирование состояния сервера.
 */
public class ServerThreadStateTest {

    /**
     * Тест, проверяет, что после команды hard stop и установленном обычном состоянии, поток завершается.
     */
    @Test
    void shouldTerminateThreadAfterHardStopInNormalState() throws InterruptedException {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        var serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        serverThread.setEvent(event);

        // Добавляем произвольные команды
        queue.add(new PrintCommand());
        queue.add(new PrintCommand());
        // Отправляем команду жесткой остановки
        queue.add(new HardStopCommand(serverThread));
        queue.add(new PrintCommand());

        serverThread.start();

        // Ждем, пока событие не будет установлено внутри потока
        event.waitOne();

        // После этого очередь должна содержать только одну команду (PrintCommand перед остановкой или не должно быть новых элементов)
        assertEquals(1, queue.size());
        // Так как состояние не изменяли, то ничего не накопили
        assertEquals(0, serverThread.moveQueue.size());
    }

    /**
     * Написать тест, который проверяет, что после команды hard stop и установленном MoveTo состоянии, поток завершается.
     */
    @Test
    void shouldTerminateThreadAfterHardStopInMoveToState() throws InterruptedException {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        var serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        serverThread.setEvent(event);

        // заранее установим MoveTo состояние
        serverThread.setCurrentState(new MoveToState(serverThread));

        // Добавляем произвольные команды
        queue.add(new PrintCommand());
        // Отправляем команду жесткой остановки
        queue.add(new HardStopCommand(serverThread));
        queue.add(new PrintCommand());

        serverThread.start();

        // Ждем, пока событие не будет установлено внутри потока
        event.waitOne();

        // После этого очередь должна содержать только одну команду (PrintCommand перед остановкой или не должно быть новых элементов)
        assertEquals(1, queue.size());
        // Проверяем, что в moveQueue очереди должна быть одна команда после HardStop
        assertEquals(1, serverThread.moveQueue.size());
    }

    /**
     * Тест, который проверяет, что после команды MoveToCommand, поток переходит на обработку Команд с помощью состояния MoveTo.
     */
    @Test
    void shouldTransitionToMoveToStateAndTransferCommands() throws InterruptedException {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        var serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        serverThread.setEvent(event);

        // Добавляем произвольные команды
        queue.add(new PrintCommand());
        queue.add(new MoveToCommand(serverThread));
        queue.add(new PrintCommand());
        queue.add(new PrintCommand());
        queue.add(new PrintCommand());

        // Проверка, что в начале у обычного состояния 5 команда, а у Move - 0
        assertEquals(5, queue.size());
        assertEquals(0, serverThread.moveQueue.size());

        serverThread.start();

        // Ждем, пока событие не будет установлено внутри потока
        event.waitOne();

        // Проверка, что в конце у обычного состояния 0 команд, а у Move - 3
        // После команды MoveToCommand все команды сохранились в очереди moveQueue
        assertEquals(0, queue.size());
        assertEquals(3, serverThread.moveQueue.size());
    }

    /**
     * Написать тест, который проверяет, что после команды RunCommand, поток переходит на обработку Команд с помощью состояния "Обычное".
     */
    @Test
    void shouldReturnToNormalStateAndProcessCommandsAfterRunCommand() throws InterruptedException {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        var serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        serverThread.setEvent(event);

        // заранее установим MoveTo состояние
        serverThread.setCurrentState(new MoveToState(serverThread));

        // Добавляем произвольные команды
        queue.add(new PrintCommand());
        queue.add(new PrintCommand());
        queue.add(new PrintCommand());
        queue.add(new RunCommand(serverThread));
        queue.add(new PrintCommand());

        // Проверка, что в начале у обычного состояния 5 команд, а у Move - 0
        assertEquals(5, queue.size());
        assertEquals(0, serverThread.moveQueue.size());

        serverThread.start();

        // Ждем, пока событие не будет установлено внутри потока
        event.waitOne();

        // Проверка, что в конце у обычного состояния 0 команд, а у Move - 3
        // До выполнения команды RunCommand все команды сохранялись в очереди moveQueue, после - не сохраняются
        assertEquals(0, queue.size());
        assertEquals(3, serverThread.moveQueue.size());
    }
}
