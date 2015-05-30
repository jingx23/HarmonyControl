package helper;

import net.whistlingfish.harmony.HarmonyClient;
import sirius.kernel.Sirius;
import sirius.kernel.di.std.Register;

/**
 * Created by jsc on 03.05.15.
 */
@Register(classes = HarmonyFactory.class)
public class HarmonyFactory {
    private HarmonyClient harmonyClient = HarmonyClient.getInstance();
    private boolean initialized = false;

    public void initialize() {
        String ip = Sirius.getConfig().getString("harmony.ip");
        String user = Sirius.getConfig().getString("harmony.user");
        String password = Sirius.getConfig().getString("harmony.password");
        harmonyClient.connect(ip, user, password);
        initialized = true;
    }

    public HarmonyClient getClient() {
        return harmonyClient;
    }

    public boolean initialized() {
        return initialized;
    }
}
