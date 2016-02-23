package pl.parser.nbp.service.impl.nbp;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;
import org.jooq.lambda.Unchecked;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class HttpClient {
	private static final URI exchangeDataRootURI = URI.create("http://www.nbp.pl/kursy/xml/");
	private static final String allowedfileNamePrefix = "c";
	
	private final RxClient<RxCompletionStageInvoker> httpClient;
	
	public CompletableFuture<ExchangeRateTable> getExchangeRateTable(String xmlName) {
		val target = httpClient.target(exchangeDataRootURI).path(xmlName + ".xml");
		return target.request()
			.accept(MediaType.TEXT_XML_TYPE)
			.rx()
			.get(ExchangeRateTable.class)
			.whenComplete((result, ex) -> {
				if(ex != null) logger.error(() -> "Error occured when requesting: " + target.getUri(), ex);
			})
			.toCompletableFuture()
		;
	}
	
	public CompletableFuture<List<String>> getExchangeRateFileNames(String directoryName) {
		val target = httpClient.target(exchangeDataRootURI).path(directoryName + ".txt");
		return target.request()
			.accept(MediaType.TEXT_PLAIN_TYPE)
			.rx()
			.get(InputStream.class)
			.thenApply(Unchecked.function(directoryContents -> {
				try(BufferedReader br = new BufferedReader(new InputStreamReader(directoryContents))) {
					return br.lines().filter(fileName -> fileName.startsWith(allowedfileNamePrefix)).collect(toList());
				}
			}))
			.whenComplete((result, ex) -> {
				if(ex != null) logger.error(() -> "Error occured when requesting: " + target.getUri(), ex);
			})
			.toCompletableFuture()
		;
	}
}
