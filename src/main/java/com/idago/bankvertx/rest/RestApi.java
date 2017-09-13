/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.rest;

import com.idago.bankvertx.entities.dto.AccountDTO;
import com.idago.bankvertx.entities.dto.AppUserDTO;
import com.idago.bankvertx.entities.dto.ResponseDTO;
import com.idago.bankvertx.entities.dto.TransactionDTO;
import com.idago.bankvertx.service.BankAccessService;
import com.idago.bankvertx.service.BankAccountService;
import com.idago.bankvertx.service.gateway.AccessService;
import com.idago.bankvertx.service.gateway.AccountService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.rx.java.ObservableFuture;
import io.vertx.rx.java.RxHelper;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class RestApi extends AbstractVerticle {

    private final AccessService accessService;
    private final AccountService accountService;
    private final Vertx appVertx;
    private final Logger logger;

    public RestApi() {
        this.appVertx = Vertx.vertx();
        this.logger = LoggerFactory.getLogger(RestApi.class);
        this.accessService = BankAccessService.getInstance();
        this.accountService = BankAccountService.getInstance();
    }

    @Override
    public void start() {
        ///creeate a router
        Router router = Router.router(appVertx);

        ///Allow the router to retrieve the body of any request made
        router.route().handler(BodyHandler.create());

        ///create the endpoints
        //router.get("/").handler(routingCtx -> routingCtx.reroute("/todefine")); TODO -> reroute or serve a 404 PAGE ??
        router.post("/register").handler(this::registerCall);
        router.get("/authentificate/:username/:password").handler(this::loginCall);
        router.get("/accounts").handler(this::allAccountsCall);
        router.post("/accounts").handler(this::createAccountCall);
        router.delete("/accounts/:id").handler(this::deleteAccountCall);
        router.post("/transactions").handler(this::transactionOperationCall);

        //RxHelper helps to create an handler who will logger the connection state
        ObservableFuture<HttpServer> rxServer = RxHelper.observableFuture();
        rxServer.subscribe(
                server -> logger.info("Server is ready and listening on Port " + server.actualPort()),
                error -> logger.error("Oooops, Server could not start : Cause => " + error.getMessage(), error.getCause())
        );

        //finaly creating an httpServer
        HttpServerOptions config = new HttpServerOptions().setPort(8040); //// will configure the server on a specific port
        appVertx.createHttpServer(config)
                .requestHandler(router::accept) /// let the router be in charge of the requests
                .listen(rxServer.toHandler());
    }

    private void registerCall(RoutingContext ctx) {
        String userToCreateAsString = ctx.getBodyAsString();
        AppUserDTO userDTO = Json.decodeValue(userToCreateAsString, AppUserDTO.class);
        logger.info("registerCall for newUser => " + userDTO + " from DeploymentID " + this.deploymentID());
        ResponseDTO responseFromService = accessService.register(userDTO);
        if (responseFromService.isRequestDone()) {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(201)
                    .end(responseFromService.getMessage());
        } else {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(500)
                    .end(responseFromService.getMessage());
        }
    }

    private void loginCall(RoutingContext ctx) {
        String username = ctx.request().getParam("username");
        String password = ctx.request().getParam("password");
        logger.info("loginCall with username " + username + " & password => " + password + " from DeploymentID " + this.deploymentID());
        if (username != null && password != null) {
            ResponseDTO responseFromService = accessService.login(username, password);
            if (responseFromService.isRequestDone()) {
                ctx.response()
                        .putHeader("Content-type", "application/json : charset=utf-8")
                        .setStatusMessage(responseFromService.getMessage())
                        .setStatusCode(200)
                        .end(Json.encodePrettily(JsonObject.mapFrom((AppUserDTO) responseFromService.getExpectedObject())));
            } else {
                ctx.response()
                        .putHeader("Content-type", "text/plain : charset=utf-8")
                        .setStatusCode(500)
                        .end(responseFromService.getMessage());
            }
        } else {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(500)
                    .end("Null or empty parameters are not allowed");
        }
    }

    private void allAccountsCall(RoutingContext ctx) {
        logger.info("allAccountsCall from DeploymentID " + this.deploymentID());
        List<JsonObject> allAccounts = accountService.allAccounts()
                .map(JsonObject::mapFrom)
                .map(jsObj -> jsObj.put("self", ctx.request().absoluteURI() + jsObj.getInteger("id")))
                .collect(toList());
        ctx.response()
                .putHeader("Content-type", "application/json : charset=utf-8")
                .setStatusCode(allAccounts.isEmpty() ? 204 : 200)
                .end(Json.encodePrettily(allAccounts));
    }

    private void createAccountCall(RoutingContext ctx) {
        String accountToCreateAsString = ctx.getBodyAsString();
        AccountDTO accountDTO = Json.decodeValue(accountToCreateAsString, AccountDTO.class);
        logger.info("createAccountCall for appUser => " + accountDTO.getHolder() + " from DeploymentID " + this.deploymentID());
        ResponseDTO responseFromService = accountService.createClientBankAccount(accountDTO);
        if (responseFromService.isRequestDone()) {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(201)
                    .end(responseFromService.getMessage());
        } else {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(500)
                    .end(responseFromService.getMessage());
        }
    }

    private void deleteAccountCall(RoutingContext ctx) {
        String accountID = ctx.request().getParam("id");
        logger.info("deleteAccountCall with accountID " + accountID + " from DeploymentID " + this.deploymentID());
        if (accountID != null) {
            try {
                ResponseDTO responseFromService = accountService.deleteClientBankAccount(Integer.parseInt(accountID));
                if (responseFromService.isRequestDone()) {
                    ctx.response()
                            .putHeader("Content-type", "text/plain : charset=utf-8")
                            .setStatusCode(200)
                            .end(responseFromService.getMessage());
                } else {
                    ctx.response()
                            .putHeader("Content-type", "text/plain : charset=utf-8")
                            .setStatusCode(500)
                            .end(responseFromService.getMessage());
                }
            } catch (Exception e) {
                logger.error("Parameter received as ID for deleting account is not a valid Number -> ", e.getMessage());
                ctx.response()
                        .putHeader("Content-type", "text/plain : charset=utf-8")
                        .setStatusCode(500)
                        .end(e.getMessage());
            }
        } else {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(500)
                    .end("Null or empty parameters are not allowed");
        }
    }

    private void transactionOperationCall(RoutingContext ctx) {
        String transactionToProcessAsString = ctx.getBodyAsString();
        TransactionDTO transactionDTO = Json.decodeValue(transactionToProcessAsString, TransactionDTO.class);
        logger.info("transactionOperationCall : => " + transactionDTO.getTransactionType() + " to process, from DeploymentID " + this.deploymentID());
        ResponseDTO responseFromService = accountService.makeABankOperation(transactionDTO);
        if (responseFromService.isRequestDone()) {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(201)
                    .end(responseFromService.getMessage());
        } else {
            ctx.response()
                    .putHeader("Content-type", "text/plain : charset=utf-8")
                    .setStatusCode(500)
                    .end(responseFromService.getMessage());
        }
    }

}
