package pl.parser.nbp.service.impl.nbp;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.parser.nbp.service.impl.nbp.NBPExchangeRateSearchService.directoryFormat;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import lombok.val;
import pl.parser.nbp.NBPModule;
import pl.parser.nbp.jaxb.adapter.CommaBigDecimalAdapterTest;

// Tests only check wherever client works correctly. We can't expect any specific data.
@Test(groups = HttpClientTest.TEST_GROUP, dependsOnGroups = CommaBigDecimalAdapterTest.TEST_GROUP)
@Guice(modules = NBPModule.class)
public class HttpClientTest {
	public static final String TEST_GROUP = "NBPHttpClientTest";
	
	private static final String currentDirectoryName = String.format(directoryFormat, "");
	
	@Inject private HttpClient client;
	
	public void getExchangeRateFileNames() throws InterruptedException, ExecutionException {
		val result = client.getExchangeRateFileNames(currentDirectoryName).get();
		assertThat(result).isNotEmpty();
	}
	
	@Test(dependsOnMethods = "getExchangeRateFileNames")
	public void getExchangeRateTable() throws InterruptedException, ExecutionException {
		val directoryList = client.getExchangeRateFileNames(currentDirectoryName).get();
		val result = client.getExchangeRateTable(directoryList.get(0)).get();
		assertThat(result).isNotNull();
		assertThat(result.getPositions()).isNotEmpty();
	}
}
