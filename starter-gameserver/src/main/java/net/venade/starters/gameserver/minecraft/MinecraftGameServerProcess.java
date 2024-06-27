package net.venade.starters.gameserver.minecraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import lombok.Getter;
import net.venade.starters.gameserver.IGameServerProcess;
import net.venade.starters.gameserver.adress.PortGenerator;
import org.apache.commons.io.FileUtils;

/**
 * @author Nikolas Rummel
 * @since 17.04.22
 */
@Getter
public class MinecraftGameServerProcess implements IGameServerProcess {

    private final MinecraftGameServerProvider minecraftServerProvider;
    private MinecraftGameServer minecraftGameServer;
    private Thread thread;
    private Process process;
    private File folder;

    public MinecraftGameServerProcess(MinecraftGameServerProvider minecraftServerProvider,
        MinecraftGameServer minecraftGameServer) {
        this.minecraftServerProvider = minecraftServerProvider;
        this.minecraftGameServer = minecraftGameServer;
    }

    @Override
    public void prepare() {
        try {
            this.folder = new File("server//" + minecraftGameServer.getEmailOfOwner() + "//");

            if (!folder.exists()) {
                this.folder.mkdirs();
                this.copyFromTemplate();
            }
            System.out.println("COPYING TEMPLATE");
            System.out.println("FINISHED COPYING");

            final File bridge = new File(this.folder, "plugins//Bridge.jar");
            bridge.delete();

            FileUtils.copyFile(new File("data/Bridge.jar"), bridge);

            final int port = PortGenerator.generateNewPort(25565);

            minecraftGameServer.setPort(PortGenerator.generateNewPort(port));
            PortGenerator.USED_PORTS.add(port);

            //Next Step
            this.prepareServerConfiguration();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void prepareServerConfiguration() throws Exception {
        Properties properties = new Properties();

        // Server Properties
        final File serverProperties = new File(this.folder, "server.properties");

        try (InputStream inputStream = new FileInputStream(serverProperties)) {
            properties.load(inputStream);
        }

        properties.setProperty("server-name", minecraftGameServer.getServerName());
        properties.setProperty("server-port", minecraftGameServer.getPort() + "");
        properties.setProperty("server-ip", "0.0.0.0" + "");
        properties.setProperty("online-mode", "true");

        try (OutputStream outputStream = new FileOutputStream(serverProperties);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream,
                StandardCharsets.UTF_8)) {

            properties.store(writer, null);
        }

        // Eula agreement
        properties = new Properties();
        final File eulaFile = new File(this.folder, "eula.txt");

        if (eulaFile.exists()) {
            try (InputStream inputStream = new FileInputStream(eulaFile)) {
                properties.load(inputStream);
            }
        } else {
            eulaFile.createNewFile();
        }

        properties.setProperty("eula", "true");

        try (OutputStream outputStream = new FileOutputStream(eulaFile);
            OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            properties.store(outputStreamWriter, "Auto Eula agreement");
        }
    }

    //Called in Provider
    @Override
    public void start() {
        System.out.println("STARTING SERVER");
        this.thread = new Thread(() -> {
            try {
                String[] command =
                    new String[]{
                        "java",
                        "-jar",
                        "-Xmx" + minecraftGameServer.getMemory() + "M",
                        "-Xms" + minecraftGameServer.getMemory() + "M",
                        "server.jar",
                        "--nogui"
                    };

                final ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.directory(this.folder);

                this.process = processBuilder.start();
                System.out.println(
                    "Server "
                        + minecraftGameServer.getServerName()
                        + " owned by "
                        + minecraftGameServer.getEmailOfOwner()
                        + " starts now on port "
                        + minecraftGameServer.getPort()
                        + ".");

                this.minecraftServerProvider.addServerToCache(minecraftGameServer.getEmailOfOwner(), minecraftGameServer, this);
                this.process.waitFor(); //Block
                this.handleStop();
                this.stop();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        this.thread.start();
    }

    @Override
    public void stop() {
        if (!this.executeCommand("stop")) {
            this.process.destroyForcibly();
        }
        new Thread(() -> {
            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.process.isAlive()) {
                System.out.println(
                    "Server is still alive after 10 seconds. Forcibly ending process...");
                this.process.destroyForcibly();
            }
        }).start();
    }

    @Override
    public void handleStop() {
        if (this.process.isAlive()) {
            this.stop();
        }
        PortGenerator.USED_PORTS.remove(Integer.valueOf(minecraftGameServer.getPort()));
    }

    @Override
    public boolean executeCommand(String command) {
        if (this.process.isAlive()) {
            try {
                process.getOutputStream().write((command + "\n").getBytes(StandardCharsets.UTF_8));
                process.getOutputStream().flush();
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
                this.process.destroyForcibly();
                System.err.println("Broken pipe: Force destroying process.");
                return false;
            }
        }
        return false;
    }

    @Override
    public void copyFromTemplate() throws IOException {
        final File globalTemplate = new File("templates//Global//Server//");
        globalTemplate.mkdirs();


        FileUtils.copyDirectory(globalTemplate, this.folder);
    }

    @Override
    public void flushDirectory() throws IOException {
        FileUtils.deleteDirectory(this.folder);
    }
}
