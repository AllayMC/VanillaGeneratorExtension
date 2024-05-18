package com.github.coolloong.pnx;

import cn.nukkit.plugin.PluginBase;

import java.net.URLClassLoader;

public class PNXVanillaGeneratorExtension extends PluginBase {
    @Override
    public void onEnable() {
        URLClassLoader pluginClassLoader = (URLClassLoader) getPluginClassLoader();
    }
}
