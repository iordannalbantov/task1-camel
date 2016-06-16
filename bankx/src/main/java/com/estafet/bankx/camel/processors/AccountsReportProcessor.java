package com.estafet.bankx.camel.processors;

import com.estafet.bankx.model.AccountWrapper;
import com.estafet.bankx.model.AccountsReportWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountsReportProcessor implements Processor {

    private static final int LENGTH_FILE_EXTENSION = ".txt".length();

    @Override
    public void process(Exchange exchange) throws Exception {
        Object fileNameObject = exchange.getIn().getHeader(Exchange.FILE_NAME);
        if (fileNameObject instanceof String) {
            String fileName = (String) fileNameObject;
            fileName = fileName.replaceAll("\\s+", "");
            fileName = fileName.substring(0, fileName.length() - LENGTH_FILE_EXTENSION);
            if (exchange.getIn().getBody() instanceof AccountWrapper) {
                AccountWrapper accountWrapper = (AccountWrapper) exchange.getIn().getBody();
                AccountsReportWrapper accountsReportWrapper = new AccountsReportWrapper(fileName, accountWrapper);
                exchange.getIn().setBody(accountsReportWrapper);
            }
        }
    }
}
