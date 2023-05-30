package com.github.shadowsocks.plugin.shadowtls;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.github.shadowsocks.plugin.PluginOptions;


public class ConfigFragment extends PreferenceFragmentCompat {
    private EditTextPreference sni;
    private EditTextPreference password;
    private EditTextPreference threads;
    private CheckBoxPreference v3;
    private CheckBoxPreference fastopen;

    public PluginOptions getOptions() {
        PluginOptions options = new PluginOptions();
        options.put("host", this.sni.getText());
        options.put("passwd", this.password.getText());
        if (!isNullOrEmpty(this.threads.getText())) {
            options.put("threads", this.threads.getText());
        }

        if (this.v3.isChecked()) {
            options.put("v3", "1");
        }

        if (this.fastopen.isChecked()) {
            options.put("fastopen", "1");
        }

        return options;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.addPreferencesFromResource(R.xml.config);

        this.sni = this.findPreference("sni");
        this.password = this.findPreference("password");
        this.threads = this.findPreference("threads");
        this.v3 = this.findPreference("v3");
        this.fastopen = this.findPreference("fastopen");

        this.threads.setOnBindEditTextListener(it -> {
            it.setInputType(InputType.TYPE_CLASS_NUMBER);
            it.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getListView().setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getTop(), v.getPaddingRight(), insets.getSystemWindowInsetBottom());
            return insets;
        });
    }

    public void onInitializePluginOptions(PluginOptions options) {
        this.sni.setText(options.containsKey("host") ? options.get("host") : "");
        this.password.setText(options.containsKey("passwd") ? options.get("passwd") : "");
        this.threads.setText(options.containsKey("threads") ? options.get("threads") : "");
        this.v3.setChecked(options.containsKey("v3"));
        this.fastopen.setChecked(options.containsKey("fastopen"));
    }

    private static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
