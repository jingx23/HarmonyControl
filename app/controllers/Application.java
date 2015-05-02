package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helper.HarmonyInstance;
import net.whistlingfish.harmony.config.Activity;
import play.api.libs.json.JsPath;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result command() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        }
        ObjectNode result = Json.newObject();
        int deviceId = json.get("deviceId").asInt(-99);
        String command = json.get("command").asText();
        if(deviceId == -99 || command == null || "".equals(command)){
            result.put("status", "Error");
            result.put("message", "deviceId: " + deviceId + " command: " + command);
        }else{
            result.put("status", "OK");
            result.put("command", command);
            if(command.equals("switch")){
                HarmonyInstance.getClient().startActivity(deviceId);
            }else{
                HarmonyInstance.getClient().pressButton(deviceId, command);
            }
        }
        return ok(result);
    }

}
