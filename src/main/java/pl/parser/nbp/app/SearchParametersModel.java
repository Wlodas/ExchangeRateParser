package pl.parser.nbp.app;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SearchParametersModel {
	private final StringProperty currencyCode = new SimpleStringProperty();
	
	public String getCurrencyCode() { return currencyCode.get(); }
	public void setCurrencyCode(String value) { currencyCode.set(value); }
	public StringProperty currencyCodeProperty() { return currencyCode; }
	
	private final ObjectProperty<LocalDate> dateFrom = new SimpleObjectProperty<>();
	
	public LocalDate getDateFrom() { return dateFrom.get(); }
	public void setDateFrom(LocalDate value) { dateFrom.set(value); }
	public ObjectProperty<LocalDate> dateFromProperty() { return dateFrom; }
	
	private final ObjectProperty<LocalDate> dateTo = new SimpleObjectProperty<>();
	
	public LocalDate getDateTo() { return dateTo.get(); }
	public void setDateTo(LocalDate value) { dateTo.set(value); }
	public ObjectProperty<LocalDate> dateToProperty() { return dateTo; }
	
	public boolean isValid() {
		return currencyCode.isNotEmpty().get()
			&& dateFrom.isNotNull().get()
			&& dateTo.isNotNull().get();
	}
}
