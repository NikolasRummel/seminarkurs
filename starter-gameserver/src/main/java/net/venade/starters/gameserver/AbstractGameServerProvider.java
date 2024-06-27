package net.venade.starters.gameserver;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.Getter;

/**
 * @author Nikolas Rummel
 * @since 16.04.22
 */
@Getter
public abstract class AbstractGameServerProvider {

    protected final HashMap<String, AbstractGameServer> serverCache;
    protected final HashMap<String, IGameServerProcess> serverProcesses;
    protected final BlockingQueue<IGameServerProcess> queue;
    protected final int internalMemory;

    private Thread workerThread;

    protected AbstractGameServerProvider(int memory) {
        this.serverCache = new HashMap<>();
        this.serverProcesses = new HashMap<>();
        this.queue = new LinkedBlockingQueue<>();
        this.internalMemory = memory;

        this.workerThread = new Thread(()-> {
            while (true) {
                try {
                    final IGameServerProcess serverProcess = queue.take();
                    serverProcess.prepare();
                    serverProcess.start();

                } catch (InterruptedException e) {
                    System.out.println("Interrupted whilst waiting for a serverProcess!");
                }
            }
        });
        this.workerThread.start();
    }

    public void addServerToCache(String emailOfOwner, AbstractGameServer gameServer, IGameServerProcess gameServerProcess) {
        this.serverCache.put(emailOfOwner, gameServer);
        this.serverProcesses.put(emailOfOwner, gameServerProcess);
    }

    public AbstractGameServer getServerFromCache(String emailOfOwner) {
        return serverCache.get(emailOfOwner);
    }

    public IGameServerProcess getServerProcess(String emailOfOwner) {
        return serverProcesses.get(emailOfOwner);
    }

    public abstract void requestRegisterNewServer(AbstractGameServer gameServer);
    public abstract void requestStartServer(AbstractGameServer gameServer);
    public abstract void requestStopServer(AbstractGameServer gameServer);
    public abstract boolean requestCommandExecution(AbstractGameServer gameServer, String command);

    public abstract static class Builder<T extends AbstractGameServerProvider, B extends Builder<T, B>> {

        protected int internalMemory;

        protected Builder() {
        }

        protected abstract B getThis();

        public B withMemory(int memory) {
            this.internalMemory = memory;
            return getThis();
        }

        abstract public T build();
    }

}