package controllers;


import helper.HarmonyInstance;
import net.whistlingfish.harmony.config.Activity;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jsc on 26.04.15.
 */
public class Application extends Controller {
    public static Result index() {
        List<Activity> listActivities = HarmonyInstance.getClient().getConfig().getActivities();
        Collections.sort(listActivities, new Comparator<Activity>() {
            @Override
            public int compare(Activity o1, Activity o2) {
                Integer order1 = o1.getActivityOrder(), order2 = o2.getActivityOrder();
                if (order1 == null ^ order2 == null) {
                    return (order1 == null) ? -1 : 1;
                }

                if (order1 == null && order2 == null) {
                    return 0;
                }
                return order1.compareTo(order2);
            }
        });
        return ok(views.html.index.render(HarmonyInstance.getClient().getCurrentActivity(), listActivities));
    }

}
