package controllers;

import com.alibaba.fastjson.JSONObject;
import helper.HarmonyFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.whistlingfish.harmony.config.Activity;
import sirius.kernel.di.std.Register;
import sirius.kernel.health.HandledException;
import sirius.web.controller.Controller;
import sirius.web.controller.Routed;
import sirius.web.http.WebContext;
import sirius.web.services.JSONStructuredOutput;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jsc on 05.05.15.
 */
@Register
public class Application implements Controller {

    @Routed("/")
    public void index(WebContext ctx) {
        if (!HarmonyFactory.initialized()) {
            HarmonyFactory.initialize();
        }

        List<Activity> listActivities = HarmonyFactory.getClient().getConfig().getActivities();
        Collections.sort(listActivities, new Comparator<Activity>() {
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
        ctx.respondWith().template("view/index.html", HarmonyFactory.getClient().getCurrentActivity(), listActivities);
    }

    @Routed("/command")
    public static void command(WebContext ctx) {
        JSONObject json = ctx.getJSONContent();
        if (json == null) {
            ctx.respondWith().direct(HttpResponseStatus.NO_CONTENT, "empty json content");
        }
        JSONStructuredOutput output = ctx.respondWith().json();
        Integer deviceId = json.getInteger("deviceId");
        String command = json.getString("command");
        if (deviceId == null || command == null || "".equals(command)) {
            output.beginResult();
            output.property("status", "Error");
            output.property("message", "deviceId: " + deviceId + " command: " + command);
            output.endResult();
        } else {
            output.beginResult();
            output.property("status", "OK");
            output.property("command", command);
            output.endResult();
            if (command.equals("switch")) {
                HarmonyFactory.getClient().startActivity(deviceId);
            } else {
                HarmonyFactory.getClient().pressButton(deviceId, command);
            }
        }
    }

    public void onError(WebContext webContext, HandledException e) {
        webContext.respondWith().direct(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
