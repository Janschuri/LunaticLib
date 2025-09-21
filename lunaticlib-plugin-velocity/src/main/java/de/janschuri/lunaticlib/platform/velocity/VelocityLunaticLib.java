package de.janschuri.lunaticlib.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.platform.velocity.commands.VelocityCommandAdapter;
import de.janschuri.lunaticlib.platform.velocity.commands.VelocityCommandHandler;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.VelocityProxyRequestsAdapter;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.VelocityProxyRequestsHandler;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderAdapter;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderHandler;
import org.bstats.velocity.Metrics;

import java.nio.file.Path;

@Plugin(
        id = "lunaticlib",
        name = "LunaticLib",
        version = "${project.version}",
        authors = "janschuri"
)
public class VelocityLunaticLib {

    private static ProxyServer proxy;
    private static VelocityLunaticLib instance;
    private static Path dataDirectory;
    private final Metrics.Factory metricsFactory;

    @Inject
    public VelocityLunaticLib(ProxyServer proxy, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        VelocityLunaticLib.proxy = proxy;
        VelocityLunaticLib.instance = this;
        VelocityLunaticLib.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        VelocitySenderAdapter senderAdapter = new VelocitySenderAdapter(proxy);
        VelocitySenderHandler.initialize(senderAdapter);

        VelocityCommandAdapter commandAdapter = new VelocityCommandAdapter(instance, proxy);
        VelocityCommandHandler.initialize(commandAdapter, false);

        VelocityProxyRequestsAdapter proxyRequestsAdapter = new VelocityProxyRequestsAdapter(instance, proxy);
        VelocityProxyRequestsHandler.initialize(proxyRequestsAdapter, false);


        int pluginId = 21915;
        Metrics metrics = metricsFactory.make(this, pluginId);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        VelocitySenderHandler.shutdown();
        VelocityCommandHandler.shutdown();
        VelocityProxyRequestsHandler.shutdown();
    }
}
