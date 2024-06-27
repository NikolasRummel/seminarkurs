package net.venade.starters.gameserver.minecraft;

import net.venade.starters.gameserver.AbstractGameServer;
import net.venade.starters.gameserver.AbstractGameServerProvider;

/**
 * @author Nikolas Rummel
 * @since 17.04.22
 */
public class MinecraftGameServerProvider extends AbstractGameServerProvider {

    private MinecraftGameServerProvider(int memory) {
        super(memory);
    }

    @Override
    public void requestRegisterNewServer(AbstractGameServer gameServer) {
        requestStartServer(gameServer);
    }

    @Override
    public void requestStartServer(AbstractGameServer gameServer) {
        MinecraftGameServerProcess gameServerProcess = new MinecraftGameServerProcess(this,
            (MinecraftGameServer) gameServer);
        System.out.println(
            "Added " + gameServer.getServerName() + " from " + gameServer.getEmailOfOwner()
                + " in queue... starting now.");
        this.queue.add(gameServerProcess);
    }

    @Override
    public void requestStopServer(AbstractGameServer gameServer) {
        System.out.println("Stopping " + gameServer.getServerName() +
            " from " + gameServer.getEmailOfOwner() + ".");

        super.getServerProcess(gameServer.getEmailOfOwner()).stop();
        super.serverCache.remove(gameServer.getEmailOfOwner());
        super.serverProcesses.remove(gameServer.getEmailOfOwner());

    }

    @Override
    public boolean requestCommandExecution(AbstractGameServer gameServer, String command) {
        MinecraftGameServerProcess serverProcess = (MinecraftGameServerProcess) super.getServerProcess(
            gameServer.getEmailOfOwner());
        return serverProcess.executeCommand(command);
    }


    public static Builder builder() {
        return new MinecraftGameServerProvider.Builder();
    }


    public static class Builder extends
        AbstractGameServerProvider.Builder<MinecraftGameServerProvider, Builder> {

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public MinecraftGameServerProvider build() {
            return new MinecraftGameServerProvider(super.internalMemory);
        }
    }
}
