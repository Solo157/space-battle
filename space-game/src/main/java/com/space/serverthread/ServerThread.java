package com.space.serverthread;

import com.space.command.ICommand;
import com.space.command.states.State;
import com.space.command.states.UsualState;
import com.space.event.ManualResetEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;

public class ServerThread {

    public final BlockingQueue<ICommand> queue;
    public final BlockingQueue<ICommand> moveQueue;

    public Runnable behaviour;
    public Thread thread;
    private boolean stop = false;
    @Getter
    @Setter
    private ManualResetEvent event;

    @Getter
    @Setter
    private State currentState;

    public ServerThread(BlockingQueue<ICommand> q, BlockingQueue<ICommand> moveQ) {
        this.queue = q;
        this.moveQueue = moveQ;
        currentState = new UsualState(this);

        thread = new Thread(
                () -> {
                    setUpIoCScope();

                    while (!stop) {
                        State nextState = currentState.handle();
                        if (nextState == null) {
                            break;
                        }
                    }

                    actionAfterStop();
                }
        );
    }

    /**
     * Проинициализировать скоуп IoC для потока, выполняющего команды.
     */
    private void setUpIoCScope() {
        ServerThreadIoCDependencyRegistrator.registerDependency(queue);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        stop = true;
    }

    public void actionAfterStop() {
        if (event == null) {
            return;
        }

        event.unlock();
    }

    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBeviour(Runnable newBehaviour) {
        behaviour = newBehaviour;
    }

}
// имеем очередь queu в ней просто выполняются команды, затем отправляем в нее MoveToCommand с ее помощью устанавливаем
// новый контекст MoveToState при котором все последующие команды они будут не выполняться, а просто складываться в другю очередь
// как только прилетела команда RunCommand, то она переключает контекст на RunState и очередь queu продолжает выполнять команды
// прикол в том, что все это обрабатывает один тред и когда надо он выполняет команды, а когда надо сохраняет их в другой очереди
// поэтому используя команды можно выполнять команды или их сохранять
// по сути надо менять behavior