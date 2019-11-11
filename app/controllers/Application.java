package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;
import router.Routes;

public class Application extends Controller {
    public Result javascriptRoutes(Http.Request request) {
        return ok(
                // routes) instead
                JavaScriptReverseRouter.create(
                        "jsRoutes",
                        "jQuery.ajax",
                        request.host(),
                        routes.javascript.EmployeeController.saveAttendance(),
                        routes.javascript.EmployeeController.getAttendance()))
                .as(Http.MimeTypes.JAVASCRIPT);
    }
}
