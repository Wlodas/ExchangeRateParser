package pl.parser.nbp.ui.task;

import java.util.function.Consumer;

import javafx.concurrent.Task;
import javafx.stage.Stage;
import pl.parser.nbp.ui.components.ProgressIndicatorBox;

public class AsyncTask<T> {
	private final Stage indicatorWindow = ProgressIndicatorBox.create();
	private final Task<T> task;
	private final Thread thread;
	
	@FunctionalInterface
	public interface AsyncTaskSupplier<T> {
		T call() throws Exception;
	}
	
	public AsyncTask(AsyncTaskSupplier<? extends T> supplier) {
		task = new Task<T>() {
			@Override
			protected T call() throws Exception {
				return supplier.call();
			}
		};
		task.setOnRunning(handler -> indicatorWindow.show());
		task.setOnSucceeded(event -> indicatorWindow.close());
		task.setOnFailed(event -> indicatorWindow.close());
		thread = new Thread(task);
	}
	
	@SuppressWarnings("unchecked")
	public AsyncTask<T> setOnSucceded(Consumer<? super T> onSuccededListener) {
		task.setOnSucceeded(event -> {
			indicatorWindow.close();
			onSuccededListener.accept((T) event.getSource().getValue());
		});
		return this;
	}
	
	public AsyncTask<T> setOnFailed(Consumer<Throwable> onFailedListener) {
		task.setOnFailed(event -> {
			indicatorWindow.close();
			onFailedListener.accept(event.getSource().getException());
		});
		return this;
	}
	
	public AsyncTask<T> start() {
		thread.start();
		return this;
	}
}
