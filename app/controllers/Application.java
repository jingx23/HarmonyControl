package controllers;


import helper.HarmonyInstance;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by jsc on 26.04.15.
 */
public class Application extends Controller {
    public static Result index() {
        return ok(views.html.index.render(HarmonyInstance.getClient().getConfig().getActivities()));
    }

}
