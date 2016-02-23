package pl.parser.nbp;

import javax.inject.Singleton;

import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.java8.RxCompletionStage;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import pl.parser.nbp.service.ExchangeRateSearchService;
import pl.parser.nbp.service.impl.nbp.NBPExchangeRateSearchService;

public class NBPModule implements Module {
	
	@Override
	public void configure(Binder binder) {
		binder.bind(ExchangeRateSearchService.class).to(NBPExchangeRateSearchService.class);
	}
	
//	@Singleton
//	@Provides
//	XmlMapper xmlMapper() {
//		return new XmlMapper();
//	}
	
	@Singleton
	@Provides
	RxClient<RxCompletionStageInvoker> httpClient() {
		return RxCompletionStage.newClient();
	}
	
}
