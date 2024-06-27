package net.venade.starters.gameserver;

import java.io.IOException;

/**
 * @author Nikolas Rummel
 * @since 16.04.22
 */
public interface IGameServerProcess {

    void prepare();

    void prepareServerConfiguration() throws Exception;

    void start();

    void stop();

    void handleStop();

    boolean executeCommand(final String command);

    void copyFromTemplate() throws IOException;

    void flushDirectory() throws IOException;
}
