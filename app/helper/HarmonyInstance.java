package helper;

import net.whistlingfish.harmony.HarmonyClient;
import play.Play;

/**
 * Created by jsc on 26.04.15.
 */
public final class HarmonyInstance {
    private static HarmonyClient harmonyClient;

    public static void init() {
        String ip = Play.application().configuration().getString("harmony.ip");
        String user = Play.application().configuration().getString("harmony.user");
        String password = Play.application().configuration().getString("harmony.password");
        harmonyClient = HarmonyClient.getInstance();
        harmonyClient.connect(ip, user, password);
    }

    public static HarmonyClient getClient() {
        return harmonyClient;
    }
}
