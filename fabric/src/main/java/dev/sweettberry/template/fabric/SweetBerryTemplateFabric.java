package dev.sweettberry.template.fabric;

import dev.sweetberry.template.SweetBerryTemplate;
import net.fabricmc.api.ModInitializer;

public class SweetBerryTemplateFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SweetBerryTemplate.initialize();
    }
}
