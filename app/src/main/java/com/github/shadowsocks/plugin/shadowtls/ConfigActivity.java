package com.github.shadowsocks.plugin.shadowtls;

import com.github.shadowsocks.plugin.ConfigurationActivity;
import com.github.shadowsocks.plugin.PluginOptions;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

public class ConfigActivity extends ConfigurationActivity implements Toolbar.OnMenuItemClickListener  {

    private ConfigFragment child;
    private PluginOptions oldOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.child = (ConfigFragment) this.getSupportFragmentManager().findFragmentById(R.id.content);
        View v = findViewById(android.R.id.content);
        v.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), v.getPaddingBottom());
            insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom());
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(this.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_navigation_close);
        toolbar.setNavigationOnClickListener(view -> {
            this.onBackPressed();
        });
        toolbar.inflateMenu(R.menu.toolbar_config);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void onInitializePluginOptions(@NotNull PluginOptions pluginOptions) {
        this.oldOptions = pluginOptions;
        this.child.onInitializePluginOptions(pluginOptions);
    }

    @Override
    public void onBackPressed() {
        final PluginOptions options = this.child.getOptions();
        if (!options.equals(this.oldOptions)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.unsaved_changes_prompt)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        this.saveChanges(options);
                        this.finish();
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> {
                        this.finish();
                    })
                    .setNeutralButton(android.R.string.cancel, null)
                    .create()
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_apply) {
            this.saveChanges(this.child.getOptions());
            this.finish();
            return true;
        }

        return false;
    }
}