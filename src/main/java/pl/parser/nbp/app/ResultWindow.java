package pl.parser.nbp.app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.parser.nbp.service.ExchangeRateInfo;

public class ResultWindow {
	public static void display(ExchangeRateInfo info) {
		// new window
		Stage window = new Stage();
		
		// window will be modal - blocks main window
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Search results");
		window.setMinWidth(300);
		
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> window.close());
		
		VBox vBox = new VBox(20);
		vBox.getChildren().addAll(
			new Label("Currency code: " + info.getCurrencyCode()),
			new Label("Date from: " + info.getDateFrom()),
			new Label("Date to: " + info.getDateTo()),
			new Label("Buying rate (mean): " + info.getMeanBuyingRate()),
			new Label("Selling rate (standard deviation): " + info.getSellingRateStandardDeviation()),
			closeButton
		);
		vBox.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(vBox);
		window.setScene(scene);
		
		// blocks any user action until this box is shown.
		window.showAndWait();
	}
}
