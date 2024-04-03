package org.example.double_it_sts;

import jakarta.xml.soap.SOAPMessage;
import lombok.SneakyThrows;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.PrettyPrintXMLStreamWriter;
import org.apache.cxf.staxutils.StaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class CleartextLogger extends AbstractSoapInterceptor {
    private static final String LOG_SETUP = CleartextLogger.class.getName() + ".log-setup";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public CleartextLogger() {
        super(Phase.POST_PROTOCOL);
    }

    @SneakyThrows
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        boolean logged = message.containsKey(LOG_SETUP);
        if (!logged) {
            message.put(LOG_SETUP, Boolean.TRUE);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            SOAPMessage smsg = message.getContent(SOAPMessage.class);
            if (smsg != null) {
                smsg.writeTo(bout);
                log(bout.toString());
            } else {
                logger.warn("no unencrypted message content");
            }
        }
    }

    private void log(String xml) {
        StringReader in = new StringReader(xml);
        StringWriter swriter = new StringWriter();
        XMLStreamWriter xwriter = StaxUtils.createXMLStreamWriter(swriter);
        xwriter = new PrettyPrintXMLStreamWriter(xwriter, 2);
        try {
            StaxUtils.copy(new StreamSource(in), xwriter);
        } catch (XMLStreamException xse) {
            //ignore
        } finally {
            try {
                xwriter.flush();
                xwriter.close();
            } catch (XMLStreamException xse2) {
                //ignore
            }
            in.close();
        }

        String result = swriter.toString();
        logger.info(result);
    }
}
