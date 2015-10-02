package ciss.in.jena.fuseki.processors;
/**
 * Copyright 2015 DuraSpace, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Represents a Processor class that formulates a Sparql DESCRIBE query
 * that is ready to be POSTed to a Sparql endpoint.
 *
 * The processor expects the following headers:
 *      org.fcrepo.jms.identifier
 *      org.fcrepo.jms.baseURL
 * each of which can be overridden with the following:
 *      FCREPO_IDENTIFIER
 *      FCREPO_BASE_URL
 *
 * @author Aaron Coburn
 * @since November 6, 2014
 */
public class QueryProcessor implements Processor {
    /**
     *  Define how this message should be processed
     *
     *  @param exchange the current camel message exchange
     */
    public void process(final Exchange exchange) throws IOException {

        //final Message in = exchange.getIn();
        //final String subject = ProcessorUtils.getSubjectUri(in);

        Object body = exchange.getIn().getBody();
        //final StringBuilder query = new StringBuilder("update=");

        //query.append(body.toString());

        //in.setBody(query.toString());
        //exchange.getOut().setHeader("Accept", "application/rdf+xml");
        exchange.getOut().setHeader("Content-Type", "application/rdf+xml");
        //exchange.getOut().setHeader("Content-Type", "application/x-www-form-urlencoded charset=UTF-8");
        //exchange.getOut().setHeader("upload", body);
        System.out.println("body " + body);
        
        exchange.getOut().setBody(body);
    }
}