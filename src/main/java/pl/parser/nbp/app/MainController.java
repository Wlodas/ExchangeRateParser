package pl.parser.nbp.app;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pl.parser.nbp.service.ExchangeRateSearchService;
import pl.parser.nbp.ui.components.AlertBox;
import pl.parser.nbp.ui.task.AsyncTask;

public class MainController implements Initializable {
	@Inject private ExchangeRateSearchService service;
	
	@FXML private SearchParametersModel model;
	
	@FXML private TextField currencyCode;
	@FXML private DatePicker dateFrom, dateTo;
	
	@FXML
	private void handleSearchAction(ActionEvent event) {
		if(model.isValid()) {
			new AsyncTask<>(() -> service.findExchangeRate(
				model.getCurrencyCode(), 
				model.getDateFrom(),
				model.getDateTo()
			))
			.setOnSucceded(ResultWindow::display)
			.setOnFailed(ex -> AlertBox.display("Error", "Search operation wasn't completed successfully."))
			.start();
		} else {
			AlertBox.display("Warning", "You need to input all required values.");
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// limitations of current JavaFX version require to bind properties manually
		Bindings.bindBidirectional(currencyCode.textProperty(), model.currencyCodeProperty());
		Bindings.bindBidirectional(dateFrom.valueProperty(), model.dateFromProperty());
		Bindings.bindBidirectional(dateTo.valueProperty(), model.dateToProperty());
		
		// allow only letter characters for currency code
		currencyCode.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) {
				currencyCode.setText(newValue.replaceAll("[^a-zA-Z]", "").toUpperCase());
			}
		});
		
		// make DatePickers restrict each others' date ranges
		dateFrom.setDayCellFactory(datePicker -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				if(dateTo.getValue() != null && item.isAfter(dateTo.getValue())) {
					setDisable(true);
				}
			};
		});
		dateTo.setDayCellFactory(datePicker -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				if(dateFrom.getValue() != null && item.isBefore(dateFrom.getValue())) {
					setDisable(true);
				}
			};
		});
	}
}
