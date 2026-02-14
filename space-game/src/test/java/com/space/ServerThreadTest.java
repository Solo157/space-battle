package com.space;

import com.space.command.*;
import com.space.event.ManualResetEvent;
import com.space.serverthread.ServerThread;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для класса ServerThread, проверяющие корректность обработки команд и завершения работы потока.
 */
public class ServerThreadTest {

    /**
     * Тест хардстопа.
     * Проверяет, что после выполнения команды HardStop поток завершает выполнение.
     */
    @Test
    void testHardStop() throws InterruptedException {
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

        // Проверяем, что поток находится в состоянии RUNNABLE
        assertEquals(Thread.State.RUNNABLE, serverThread.thread.getState());

        // Ждем, пока событие не будет установлено внутри потока
        event.waitOne();

        // После этого очередь должна содержать только одну команду (PrintCommand перед остановкой или не должно быть новых элементов)
        assertEquals(1, queue.size());
    }

    /**
     * Тест софтстопа.
     * Проверяет, что после выполнения команды SoftStop поток завершает работу только после выполнения всех задач.
     */
    @Test
    void testSoftStop() throws InterruptedException {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        var serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        serverThread.setEvent(event);

        // Добавляем команды
        queue.add(new PrintCommand());
        queue.add(new PrintCommand());

        // Команда мягкой остановки
        queue.add(new SoftStopCommand(serverThread));
        queue.add(new PrintCommand());

        serverThread.start();

        // Проверяем, что поток находится в состоянии RUNNABLE
        assertEquals(Thread.State.RUNNABLE, serverThread.thread.getState());

        // Ждем, пока поток завершит работу
        event.waitOne();

        // Проверяем, что очередь пуста (все команды выполнены)
        assertEquals(0, queue.size());
    }

    /**
     * Тест обработки исключений внутри команд.
     * Проверяет, что исключения внутри команд не прерывают работу потока, а поток продолжает обработку следующей команды.
     */
    @Test
    void testContinueOnException() throws InterruptedException {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        var serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        serverThread.setEvent(event);

        // Добавляем команду, выбрасывающую исключение
        queue.add(new PrintCommand());
        queue.add(new RuntimeExceptionCommand()); // должна выбросить исключение внутри выполнения команды
        queue.add(new PrintCommand());

        serverThread.start();

        // Ждем завершения работы потока
        event.waitOne();

        // Проверяем, что очередь пуста (все команды, включая команду с исключением, обработаны)
        assertEquals(0, queue.size());
    }
}