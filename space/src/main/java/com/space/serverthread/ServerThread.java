package com.space.serverthread;

import com.space.command.ICommand;
import com.space.command.InitCommand;
import com.space.event.ManualResetEvent;
import com.space.ioc.IoC;

import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

public class ServerThread {
    public final BlockingQueue<ICommand> queue;
    public Runnable behaviour;
    public Thread thread;
    private boolean stop = false;
    private ManualResetEvent event;

    public ServerThread(BlockingQueue<ICommand> q) {
        queue = q;

        behaviour = () -> {
            // если очередь пустая, то вызывающий поток можно отпустить.
            if (queue.isEmpty()) {
                actionAfterStop();
            }

            ICommand cmd = null;
            try {
                cmd = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                cmd.execute();
            } catch (Exception e) {
                IoC.<String>resolve("ExceptionHandler", e);
            }
        };

        thread = new Thread(
                () -> {
                    setUpIoCScope();

                    while (!stop) {
                        behaviour.run();
                    }

                    actionAfterStop();
                }
        );
    }

    /**
     * Проинициализировать скоуп IoC для потока, выполняющего команды.
     */
    private void setUpIoCScope() {
        new InitCommand().execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();

        IoC.<ICommand>resolve("IoC.Register", "ExceptionHandler", (Function<Object[], Object>) (args) -> {
            System.out.println("Throw exception in command");
            return (Object) args[0];
        }).execute();
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        stop = true;
    }

    public void setEvent(ManualResetEvent event) {
        this.event = event;
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
