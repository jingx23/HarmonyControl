package helper;

import net.whistlingfish.harmony.HarmonyClient;
import net.whistlingfish.harmony.HarmonyClientModule;
import play.Play;

/**
 * Created by jsc on 03.05.15.
 */
public class HarmonyFactory {
    private static HarmonyClient harmonyClient = HarmonyClient.getInstance();

    public static void initialize(){
        String ip = Play.application().configuration().getString("harmony.ip");
        String user = Play.application().configuration().getString("harmony.user");
        String password = Play.application().configuration().getString("harmony.password");
        harmonyClient.connect(ip, user, password);
    }

    public static HarmonyClient getClient(){
        return harmonyClient;
    }

}
