package controllers;

import com.alibaba.fastjson.JSONObject;
import helper.HarmonyFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.whistlingfish.harmony.config.Activity;
import sirius.kernel.commons.Strings;
import sirius.kernel.di.std.Part;
import sirius.kernel.di.std.Register;
import sirius.kernel.health.Exceptions;
import sirius.kernel.health.HandledException;
import sirius.kernel.health.Log;
import sirius.web.controller.Controller;
import sirius.web.controller.Routed;
import sirius.web.http.WebContext;
import sirius.web.services.JSONStructuredOutput;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Register
public class Application implements Controller {

    public static final Log LOG = Log.get("harmony");

    @Part
    private HarmonyFactory harmony;

    @Routed("/")
    public void index(WebContext ctx) {
        ctx.respondWith().template("view/loading.html", "Getting your Hub up and running");
    }

    @Routed("/initialize")
    public void initialize(WebContext ctx) {
        if (!harmony.initialized()) {
            try {
                harmony.initialize();
            } catch (RuntimeException e) {
                ctx.respondWith().template("view/error.html", "A ninja stole your Harmony Hub");
                return;
            }
        }

        List<Activity> listActivities = harmony.getClient().getConfig().getActivities();
        Collections.sort(listActivities, (o1, o2) -> {
            Integer order1 = o1.getActivityOrder(), order2 = o2.getActivityOrder();
            if (order1 == null ^ order2 == null) {
                return (order1 == null) ? -1 : 1;
            }

            if (order1 == null && order2 == null) {
                return 0;
            }
            return order1.compareTo(order2);
        });
        try {
            ctx.respondWith().template("view/control.html", harmony.getClient().getCurrentActivity(), listActivities);
        } catch (RuntimeException e) {
            ctx.respondWith().template("view/error.html", "A ninja stole your Harmony Hub");
        }
    }

    @Routed("/command")
    public void command(WebContext ctx) {
        try {
            if (!ctx.hasContent()) {
                error(ctx, "No JSON content sent");
                return;
            }
            JSONObject json = ctx.getJSONContent();
            if (json == null) {
                error(ctx, "Emtpy JSON content sent");
                return;
            }
            Integer deviceId = json.getInteger("deviceId");
            String command = json.getString("command");
            if (deviceId == null || command == null || Strings.isEmpty(command)) {
                error(ctx, "deviceId: " + deviceId + " command: " + command);
            } else {
                JSONStructuredOutput output = ctx.respondWith().json();
                output.beginResult();
                output.property("status", "OK");
                output.property("command", command);
                output.endResult();
                if (command.equals("switch")) {
                    harmony.getClient().startActivity(deviceId);
                } else {
                    harmony.getClient().pressButton(deviceId, command);
                }
            }
        } catch (RuntimeException e) {
            LOG.WARN("Failed to send command to harmony hub.");
            error(ctx, "Failed to send command to harmony hub.");
        }
    }

    private void error(WebContext ctx, String message) {
        JSONStructuredOutput output = ctx.respondWith().json();
        output.beginResult();
        output.property("status", "ERROR");
        output.property("message", message);
        output.endResult();
    }

    @Override
    public void onError(WebContext ctx, HandledException e) {
        ctx.respondWith().direct(HttpResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
