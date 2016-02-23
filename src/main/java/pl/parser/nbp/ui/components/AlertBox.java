package pl.parser.nbp.ui.components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	public static void display(String title, String message) {
		// new window
		Stage window = new Stage();
		
		// window will be modal - blocks main window
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(300);
		
		Label label = new Label(message);
		
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> window.close());
		
		VBox vBox = new VBox(20);
		vBox.getChildren().addAll(label, closeButton);
		vBox.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(vBox);
		window.setScene(scene);
		
		// blocks any user action until this box is shown.
		window.showAndWait();
	}
}