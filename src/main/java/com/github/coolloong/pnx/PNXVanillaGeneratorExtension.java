package com.github.coolloong.pnx;

import cn.nukkit.level.generator.Generator;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.registry.RegisterException;
import cn.nukkit.registry.Registries;
import io.papermc.paperclip.Paperclip;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class PNXVanillaGeneratorExtension extends PluginBase {
    public static final String WORK_PATH = "jegenerator";

    public static void setup(URLClassLoader classLoader) {
        File file = Path.of(WORK_PATH).toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        Paperclip.setup(classLoader, new String[]{WORK_PATH, "pnx", "--noconsole", "--nogui", "--universe=jegenerator"});
    }

    public static void waitStart() {
        spinUntil(() -> !System.getProperties().getOrDefault("complete_start", false).equals("true"), Duration.of(20, ChronoUnit.MILLIS));
    }

    public static void spinUntil(Supplier<Boolean> end, Duration interval) {
        while (end.get()) {
            try {
                long times = MILLISECONDS.convert(interval);
                Thread.sleep(times);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onEnable() {
        URLClassLoader pluginClassLoader = (URLClassLoader) getPluginClassLoader();
        setup(pluginClassLoader);
        waitStart();
        try {
            final Class<?> vanillaGenerator = Class.forName("org.allaymc.jegenerator.PNXVanillaGenerator", true, pluginClassLoader);
            Registries.GENERATOR.register("vanilla", (Class<? extends Generator>) vanillaGenerator);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RegisterException e) {
            throw new RuntimeException(e);
        }
    }
}
