package pl.parser.nbp;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NBPJavaFxApplication extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Injector injector = Guice.createInjector(new NBPModule());
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(injector::getInstance);
		
		Parent root = loader.load(getClass().getResourceAsStream("/fxml/Scene.fxml"));
		
//		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
		
		Scene scene = new Scene(root);
//		scene.getStylesheets().add("/styles/Styles.css");
		
		stage.setTitle("Exchange Rate Parser");
		stage.setScene(scene);
		stage.show();
	}
	
}
