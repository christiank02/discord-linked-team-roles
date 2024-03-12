package de.aimless.aimless_java_bot.http.route;

import net.dv8tion.jda.api.requests.Route;

import static net.dv8tion.jda.api.requests.Method.GET;
import static net.dv8tion.jda.api.requests.Method.PUT;

public class ExtendedSelfRoute {

    private ExtendedSelfRoute() {}

    public static final Route GET_CURRENT_USER_APPLICATION_ROLE_CONNECTION = Route.custom(GET, "users/@me/applications/{application.id}/role-connection");
    public static final Route UPDATE_CURRENT_USER_APPLICATION_ROLE_CONNECTION = Route.custom(PUT, "users/@me/applications/{application.id}/role-connection");

}
