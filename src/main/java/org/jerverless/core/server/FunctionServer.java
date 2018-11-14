/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jerverless.core.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jerverless.boot.config.ServerConfig;
import org.jerverless.core.console.ServerConsole;

/**
 *
 * @author shalithasuranga
 */
public class FunctionServer implements IFunctionServer {
    
    private static HttpServer serverInstance = null;
    private static FunctionServer instance = null;
    private static ServerConsole consoleInstance = null;
    private static ServerConfig config = null;
    
    public FunctionServer(){
        try {
            config = ServerConfig.create();
            serverInstance = HttpServer.create(new InetSocketAddress(config.getFunctionPort().getPort()), 0);
            serverInstance.setExecutor(null); // for now single threaded
            serverInstance.createContext("/function", new FunctionHandler());
            consoleInstance = ServerConsole.getInstance(this);
        } catch (IOException ex) {
            Logger.getLogger(FunctionServer.class.getName()).log(Level.SEVERE, 
                    null, ex);
            Logger.getLogger(FunctionServer.class.getName()).log(Level.SEVERE,
                    "jerverless can't bind in to given port");
            System.exit(0);
        }
    }

    public ServerConfig getConfig() {
        return config;
    }
    
    public static FunctionServer create() {
        if(instance == null) {
            instance = new FunctionServer();
        }
        return instance;
    }
    
    public void start() {
        serverInstance.start();
        consoleInstance.openStream();
    }

    public static HttpServer getServerInstance() {
        return serverInstance;
    }

    public static FunctionServer getInstance() {
        return instance;
    }

    public static ServerConsole getConsoleInstance() {
        return consoleInstance;
    }

    public void stop() {
        System.out.println("Shutting down..");
    }

    public void restart() {
        System.out.println("Restarting..");
    }
}
