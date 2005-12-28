/** 
 * 
 * Copyright 2004 Protique Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 * 
 **/
package org.activemq.transport.http;

import org.activeio.command.WireFormat;
import org.activemq.transport.MutexTransport;
import org.activemq.transport.ResponseCorrelator;
import org.activemq.transport.Transport;
import org.activemq.transport.TransportFactory;
import org.activemq.transport.TransportServer;
import org.activemq.transport.util.TextWireFormat;
import org.activemq.transport.xstream.XStreamWireFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * @version $Revision$
 */
public class HttpTransportFactory extends TransportFactory {
    private static final Log log = LogFactory.getLog(HttpTransportFactory.class);

    public TransportServer doBind(String brokerId, URI location) throws IOException {
        return new HttpTransportServer(location);
    }

    protected TextWireFormat asTextWireFormat(WireFormat wireFormat) {
        if (wireFormat instanceof TextWireFormat) {
            return (TextWireFormat) wireFormat;
        }
        log.trace("Not created with a TextWireFromat: " + wireFormat);
        return new XStreamWireFormat();
    }

    protected String getDefaultWireFormatType() {
        return "xstream";
    }

    protected Transport createTransport(URI location, WireFormat wf) throws MalformedURLException {
        TextWireFormat textWireFormat = asTextWireFormat(wf);
        Transport transport = new HttpClientTransport(textWireFormat, location);
        //Transport transport = new HttpTransport(textWireFormat, location);
        transport = new MutexTransport(transport);
        transport = new ResponseCorrelator(transport);
        return transport;
    }

}
