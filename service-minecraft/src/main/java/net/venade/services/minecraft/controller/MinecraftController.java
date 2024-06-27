package net.venade.services.minecraft.controller;

import net.venade.services.minecraft.gameserver.MinecraftGameServerHolder;
import net.venade.services.minecraft.repository.MinecraftRepository;
import net.venade.services.minecraft.requests.ExecuteCommandRequest;
import net.venade.services.minecraft.requests.GetServerRequest;
import net.venade.services.minecraft.requests.RegisterNewServerRequest;
import net.venade.services.minecraft.requests.StartServerRequest;
import net.venade.services.minecraft.requests.StopServerRequest;
import net.venade.starters.auth.AuthProvider;
import net.venade.starters.gameserver.minecraft.MinecraftGameServer;
import net.venade.starters.httpserver.http.HttpMethod;
import net.venade.starters.httpserver.http.HttpRequest;
import net.venade.starters.httpserver.http.HttpResponse;
import net.venade.starters.httpserver.http.HttpStatus;
import net.venade.starters.httpserver.http.annotation.HttpController;
import net.venade.starters.httpserver.http.annotation.HttpMapping;
import net.venade.starters.models.ServiceRegistry;

/**
 * @author Nikolas Rummel
 * @since 07.03.2022
 */
@HttpController
public class MinecraftController {

    private MinecraftRepository repository = ServiceRegistry.getProvider(MinecraftRepository.class);
    private AuthProvider authProvider = ServiceRegistry.getProvider(AuthProvider.class);

    @HttpMapping(path = "/minecraft/get/", method = HttpMethod.POST)
    public MinecraftGameServer handleGetRequest(HttpRequest request, HttpResponse response) {
        GetServerRequest getServerRequest = request.getBodyAsObject(GetServerRequest.class);
        final String email = getServerRequest.getEmail();

        MinecraftGameServer minecraftGameServer = repository.getMinecraftServerModel(email);

        if(minecraftGameServer == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return null;

        }else return minecraftGameServer;
    }

    @HttpMapping(path = "/minecraft/registerNew/", method = HttpMethod.POST)
    public String handleRegisterNew(HttpRequest request, HttpResponse response) {
        RegisterNewServerRequest registerNewServerRequest = request.getBodyAsObject(
            RegisterNewServerRequest.class);

        //Check if user already has a server

        MinecraftGameServer gameServer = new MinecraftGameServer(
            registerNewServerRequest.getEmail(), registerNewServerRequest.getServerName(), 1024,
            123, 10, 0, registerNewServerRequest.getMotd(), "NEW_CREATED");

        repository.saveServer(gameServer);


        return "Successfully registered your new minecraft server";
    }

    @HttpMapping(path = "/minecraft/start/", method = HttpMethod.POST)
    public String handleRequestStart(HttpRequest request, HttpResponse response) {
        StartServerRequest startServerRequest = request.getBodyAsObject(StartServerRequest.class);

        //Check if user dont have a server --> cannot happen but for safety ---->>> Return fatal error
        MinecraftGameServer gameServer = repository.getMinecraftServerModel(startServerRequest.getEmail());

        MinecraftGameServerHolder.getServerProvider().requestStartServer(gameServer);
        return "Successfully started your server";
    }

    @HttpMapping(path = "/minecraft/stop/", method = HttpMethod.POST)
    public String handleRequestStop(HttpRequest request, HttpResponse response) {
        StopServerRequest stopServerRequest = request.getBodyAsObject(StopServerRequest.class);

        //Check if user dont have a server --> cannot happen but for safety ---->>> Return fatal error
        //Check if server is currently running

        MinecraftGameServer gameServer = repository.getMinecraftServerModel(stopServerRequest.getEmail());
        MinecraftGameServerHolder.getServerProvider().requestStopServer(gameServer);
        return "";
    }

    @HttpMapping(path = "minecraft/executeCommand/", method = HttpMethod.POST)
    public boolean handleRequestExecuteCommand(HttpRequest request, HttpResponse response) {
        ExecuteCommandRequest executeCommandRequest = request.getBodyAsObject(ExecuteCommandRequest.class);
        MinecraftGameServer gameServer = repository.getMinecraftServerModel(executeCommandRequest.getEmail());

        return MinecraftGameServerHolder.getServerProvider().requestCommandExecution(gameServer, executeCommandRequest.getCommand());
    }


}
