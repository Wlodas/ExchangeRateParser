package pl.parser.nbp.ui.components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressIndicatorBox {
	public static Stage create() {
		return create("Please wait...");
	}
	
	public static Stage create(String message) {
		// new window
		Stage window = new Stage();
		
		// window will be modal - blocks main window
		window.initModality(Modality.APPLICATION_MODAL);
		window.initStyle(StageStyle.UNDECORATED);
		
		Label label = new Label(message);
		ProgressIndicator progress = new ProgressIndicator();
		
		VBox vBox = new VBox(20);
		vBox.getChildren().addAll(label, progress);
		vBox.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(vBox);
		window.setScene(scene);
		
		return window;
	}
}