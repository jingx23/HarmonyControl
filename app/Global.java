import helper.HarmonyInstance;
import play.Application;
import play.GlobalSettings;

/**
 * Created by jsc on 26.04.15.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        super.onStart(application);
        HarmonyInstance.init();
    }
}
