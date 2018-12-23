package fxlauncher;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class DefaultUIProvider implements UIProvider {
	private ProgressBar progressBar;
	private Label fileLabel;
	private DecimalFormat df = new DecimalFormat("#.##");

	public Parent createLoader() {
		StackPane root = new StackPane(new ProgressIndicator());
		root.setPrefSize(600, 400);
		root.setPadding(new Insets(10));
		return root;
	}

	public Parent createUpdater(FXManifest manifest) {
		progressBar = new ProgressBar();
		progressBar.setStyle(manifest.progressBarStyle);

		Label label = new Label(manifest.updateText);
		label.setStyle(manifest.updateLabelStyle);

		fileLabel = new Label();
		fileLabel.setStyle(manifest.updateLabelStyle);

		VBox wrapper = new VBox(label, fileLabel, progressBar);
		wrapper.setStyle(manifest.wrapperStyle);

		return wrapper;
	}

	@Override
	public void updateProgress(double progress, String currentArchive, double currentMB, double totalMB) {
		fileLabel.setText(currentArchive + " " + df.format(currentMB) + "MB | " + df.format(totalMB) + " MB");
		progressBar.setProgress(progress);
	}

	public void init(Stage stage) {

	}
}
