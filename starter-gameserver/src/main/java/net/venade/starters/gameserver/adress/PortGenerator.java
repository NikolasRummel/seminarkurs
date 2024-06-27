package net.venade.starters.gameserver.adress;

import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nikolas Rummel
 * @since 17.04.22
 */
public class PortGenerator {

    public static final List<Integer> USED_PORTS = new LinkedList<>();

    public static int generateNewPort(int start) {
        if (USED_PORTS.contains(start)) {
            return generateNewPort(start + 1);
        }
        try {
            new ServerSocket(start).close();
            return start;
        } catch (Exception exception) {
            return generateNewPort(start + 1);
        }
    }

}
