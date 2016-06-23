package com.estafet.bankx.camel.processor;

import com.estafet.bankx.camel.Utils;
import com.estafet.bankx.camel.base.BankXServerTestSupport;
import com.estafet.bankx.camel.routes.BankXReportsRouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yordan Nalbantov.
 */
public class BankXReportsRouteBuilderTest extends BankXServerTestSupport {

    private static final int ASSERT_PERIOD = 10_000;
    private static final long WAIT_TIMEOUT = 5000L;

    @Test
    public void testRouteScan() throws Exception {

        // Lookup roots.

        RouteDefinition routeDefinition = context.getRouteDefinition("scan");

        // Prepare test data.

        Map<String, String> challenges = new LinkedHashMap<>();
        challenges.put("2016 06 17 17 55 03 078.txt", Utils.resource("payload//route//sftp//scan//2016 06 17 17 55 03 078.txt"));
        challenges.put("2016 06 17 17 56 44 441.txt", Utils.resource("payload//route//sftp//scan//2016 06 17 17 56 44 441.txt"));
        challenges.put("2016 06 17 17 56 46 719.txt", Utils.resource("payload//route//sftp//scan//2016 06 17 17 56 46 719.txt"));

//        IbanWrapper challengeIbanWrapper = Utils.jsonFromString(challenge, IbanWrapper.class);

        // Prepare test scenario.

        MockEndpoint mockResult = getMockEndpoint("mock:scanResult");
        // Test setting the header value.
//        mockResult.expectedMessagesMatches(mockResult.allMessages().simple("${in.header.IbanTimestampOfRequest.length}").isEqualTo(23));
//        // Test the splitting. It seams that there is no way to assure that exactly N messages have arrived.
        mockResult.expectedMessageCount(1);
        mockResult.setAssertPeriod(ASSERT_PERIOD);
        // Test the IBAN values.
//        mockResult.expectedBodiesReceived(challengeIbanWrapper.getIbans());

        routeDefinition.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:scan");
                weaveById("scanResult").replace().to("mock:scanResult");
            }
        });

        // Challenge the route.

        for (Map.Entry<String, String> entry : challenges.entrySet()) {
            Object result = template.sendBodyAndHeader("direct:scan", ExchangePattern.InOut, entry.getValue(), Exchange.FILE_NAME, entry.getKey());
        }

        // Assert results.

        assertMockEndpointsSatisfied();
    }

    /**
     * Creating the BankX`s route builder.
     *
     * @return The actual RouteBuilder instance.
     * @throws Exception On any setup or configuration problem.
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new BankXReportsRouteBuilder();
    }
}