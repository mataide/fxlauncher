package fxlauncher.emasters;

import com.install4j.api.context.UserCanceledException;
import com.install4j.api.launcher.ApplicationLauncher;
import com.install4j.api.launcher.Variables;
import com.install4j.api.update.ApplicationDisplayMode;
import com.install4j.api.update.UpdateChecker;
import com.install4j.api.update.UpdateDescriptor;
import com.install4j.api.update.UpdateDescriptorEntry;

import java.io.IOException;
import java.util.logging.Logger;

public class EmastersUpdate {

    private static final Logger log = Logger.getLogger("Launcher");

    private static final String CURRENT_VERSION = "sys.version";
    private static final String UPDATE_URL = "sys.updatesUrl";

    public void checkUpdate(Runnable callback) {
        try {
            UpdateDescriptor updateDescriptor = UpdateChecker.getUpdateDescriptor(Variables.getCompilerVariable(UPDATE_URL), ApplicationDisplayMode.UNATTENDED);
            if (updateDescriptor.getEntries().length > 0) {
                UpdateDescriptorEntry entry = updateDescriptor.getEntries()[0];
                String currentVersion = Variables.getCompilerVariable(CURRENT_VERSION);
                String newVersion = entry.getNewVersion();
                if (!currentVersion.equals(newVersion)) {
                    System.setProperty("javafx.embed.singleThread", "true");
                    callUpdate();
                }
            }
            callback.run();
        } catch (UserCanceledException | IOException ex) {
            callback.run();
            log.log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void callUpdate() {
        ApplicationLauncher.launchApplicationInProcess("284", null, new ApplicationLauncher.Callback() {
                    @Override
                    public void exited(int exitValue) {
                        if (exitValue != 0) {
                            System.exit(0);
                        }
                    }

                    @Override
                    public void prepareShutdown() {
                        System.exit(0);
                    }

                }, ApplicationLauncher.WindowMode.FRAME, null
        );
    }
}
