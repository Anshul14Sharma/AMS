package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.action.EmpCache;
import dao.iEmployeeFinder;
import models.Employee;
import models.Role;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class LoginController extends Controller {
    @Inject
    private iEmployeeFinder finder;

    @Inject
    private EmpCache empCache;

    public Result registerPageRender() {
        return ok(views.html.register.render());
    }

    public Result register() {
        JsonNode form = request().body().asJson();
        try {
            if (form != null) {
                String email = form.get("email").asText();
                Employee alreadyExist = finder.byEmail(email);
                if (alreadyExist != null) {
                    return ok("409");
                }
                String pwd = form.get("password").asText();
                String fName = form.get("firstName").asText();
                String lName = form.get("lastName").asText();
                long roleId = form.get("role").asLong();
                Role role = Role.roleFind.byId(roleId);
                Employee emp = new Employee();
                emp.setEmail(email);
                emp.setFirstName(fName);
                emp.setLastName(lName);
                emp.setPassword(pwd);
                emp.setRole(role);
                emp.save();
                return ok("200");
            } else {
                return ok("500");
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }

    public Result loginPageRender() {
        return ok(views.html.login.render());
    }

    public Result login(Http.Request request) {
        JsonNode form = request().body().asJson();
        try {
            if (form != null) {
                String email = form.get("email").asText();
                String pwd = form.get("password").asText();
                Employee alreadyExist = finder.byEmailAndPwd(email, pwd);
                if (alreadyExist != null) {
                    empCache.setSyncCacheApi(alreadyExist.getEmail());
                    if (alreadyExist.getRole().getRoleId() == 1) {
                        return ok("201").addingToSession(request, "connected", email).addingToSession(request, "role", alreadyExist.getRole().getRole());
                    }
                    return ok("200").addingToSession(request, "connected", email).addingToSession(request, "role", alreadyExist.getRole().getRole());
                } else {
//                return redirect(routes.LoginController.loginPageRender()).flashing("danger", "Incorrect email or password.");
                    return ok("401");
                }
            } else {
                return ok("500");
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }

    public Result logout(Http.Request request) {
        Logger.info("Removing the login session::{}", request.session().getOptional("connected"));
        empCache.removeCache();
        return redirect(routes.LoginController.loginPageRender()).removingFromSession(request, "connected", "role");
    }

}
