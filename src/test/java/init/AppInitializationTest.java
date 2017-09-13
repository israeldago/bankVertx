/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
@RunWith(VertxUnitRunner.class)
public class AppInitializationTest extends AbstractVerticle {

    private Vertx vertx;    

    @Before
    public void before(TestContext context) {
        //initializing the vertx obj, then creating an http server
        this.vertx = Vertx.vertx(); 
        this.vertx.createHttpServer()
                .requestHandler(req -> req.response().end("welcome ivoireNoire"))
                .listen(8080, context.asyncAssertSuccess());
    }

    @Test
    public void testHTTPCall(TestContext context) {
        // Send a request for getting the response from the http server
        HttpClient client = this.vertx.createHttpClient();
        Async async = context.async();
        client.getNow(8080, "localhost", "/", resp -> {
            resp.bodyHandler(body -> {
                context.assertEquals("welcome ivoireNoire", body.toString());
                client.close();
                async.complete();
            });
        });
    }

    @Test
    public void deployThenUndeployRestApi(TestContext context) {
        // Deploy and undeploy the RestApi verticle
        this.vertx.deployVerticle("com.idago.bankvertx.rest.RestApi", context.asyncAssertSuccess(deploymentID -> {
            this.vertx.undeploy(deploymentID, context.asyncAssertSuccess());
        }));
    }
    
    @After
    public void after(TestContext context) {
        //test for closing the vertx
        this.vertx.close(context.asyncAssertSuccess());
    }
}
