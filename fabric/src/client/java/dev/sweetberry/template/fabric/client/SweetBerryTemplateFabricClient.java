package dev.sweetberry.template.fabric.client;

import dev.sweetberry.template.client.SweetBerryTemplateClient;
import net.fabricmc.api.ClientModInitializer;

public class SweetBerryTemplateFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SweetBerryTemplateClient.initialize();
    }
}
