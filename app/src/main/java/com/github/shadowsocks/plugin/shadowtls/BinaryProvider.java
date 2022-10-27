package com.github.shadowsocks.plugin.shadowtls;

import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.github.shadowsocks.plugin.NativePluginProvider;
import com.github.shadowsocks.plugin.PathProvider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;

public class BinaryProvider extends NativePluginProvider {
    @NotNull
    @Override
    public ParcelFileDescriptor openFile(@NotNull Uri uri) {
        try {
            return ParcelFileDescriptor.open(new File(this.getExecutable()), ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {}

        return null;
    }

    @Override
    protected void populateFiles(@NotNull PathProvider pathProvider) {
        pathProvider.addPath("shadow-tls", 0b111101101);
    }

    @NotNull
    @Override
    public String getExecutable() {
        return this.getContext().getApplicationInfo().nativeLibraryDir + "/libshadow-tls.so";
    }
}
