package helper;

import net.whistlingfish.harmony.HarmonyClient;
import sirius.kernel.Sirius;

/**
 * Created by jsc on 03.05.15.
 */
public class HarmonyFactory {
    private static HarmonyClient harmonyClient = HarmonyClient.getInstance();
    private static boolean initialized = false;

    public static void initialize() {
        String ip = Sirius.getConfig().getString("harmony.ip");
        String user = Sirius.getConfig().getString("harmony.user");
        String password = Sirius.getConfig().getString("harmony.password");
        harmonyClient.connect(ip, user, password);
        initialized = true;
    }

    public static HarmonyClient getClient() {
        return harmonyClient;
    }

    public static boolean initialized() {
        return initialized;
    }
}
