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
        BlockingQueue<ICommand> q = new ArrayBlockingQueue<>(100);
        var st = new ServerThread(q);
        var event = new ManualResetEvent();
        st.setEvent(event);

        // Добавляем произвольные команды
        q.add(new PrintCommand());
        q.add(new PrintCommand());

        // Отправляем команду жесткой остановки
        q.add(new HardStopCommand(st));
        // q.add(new SetHookAfterStopCommand(st)); // закомментировано
        q.add(new PrintCommand());

        st.start();

        // Проверяем, что поток находится в состоянии RUNNABLE
        assertEquals(Thread.State.RUNNABLE, st.thread.getState());

        // Ждем, пока событие не будет установлено внутри потока
        event.waitOne();

        // После этого очередь должна содержать только одну команду (PrintCommand перед остановкой или не должно быть новых элементов)
        assertEquals(1, q.size());
    }

    /**
     * Тест софтстопа.
     * Проверяет, что после выполнения команды SoftStop поток завершает работу только после выполнения всех задач.
     */
    @Test
    void testSoftStop() throws InterruptedException {
        BlockingQueue<ICommand> q = new ArrayBlockingQueue<>(100);
        var st = new ServerThread(q);
        var event = new ManualResetEvent();
        st.setEvent(event);

        // Добавляем команды
        q.add(new PrintCommand());
        q.add(new PrintCommand());

        // Команда мягкой остановки
        q.add(new SoftStopCommand(st));
        q.add(new PrintCommand());

        st.start();

        // Проверяем, что поток находится в состоянии RUNNABLE
        assertEquals(Thread.State.RUNNABLE, st.thread.getState());

        // Ждем, пока поток завершит работу
        event.waitOne();

        // Проверяем, что очередь пуста (все команды выполнены)
        assertEquals(0, q.size());
    }

    /**
     * Тест обработки исключений внутри команд.
     * Проверяет, что исключения внутри команд не прерывают работу потока, а поток продолжает обработку следующей команды.
     */
    @Test
    void testContinueOnException() throws InterruptedException {
        BlockingQueue<ICommand> q = new ArrayBlockingQueue<>(100);
        var st = new ServerThread(q);
        var event = new ManualResetEvent();
        st.setEvent(event);

        // Добавляем команду, выбрасывающую исключение
        q.add(new PrintCommand());
        q.add(new RuntimeExceptionCommand()); // должна выбросить исключение внутри выполнения команды
        q.add(new PrintCommand());

        st.start();

        // Ждем завершения работы потока
        event.waitOne();

        // Проверяем, что очередь пуста (все команды, включая команду с исключением, обработаны)
        assertEquals(0, q.size());
    }
}