package dev.sweetberry.template.neoforge.client;

import dev.sweetberry.template.client.SweetBerryTemplateClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(
        value = "sweet_berry_template",
        dist = Dist.CLIENT
)
public class SweetBerryTemplateNeoforgeClient {
    public SweetBerryTemplateNeoforgeClient() {
        SweetBerryTemplateClient.initialize();
    }
}
