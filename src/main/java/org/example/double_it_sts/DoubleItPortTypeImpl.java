package org.example.double_it_sts;

import jakarta.jws.WebService;
import org.example.contract.doubleit.DoubleItPortType;
import org.springframework.stereotype.Service;

@Service("service")
@WebService(targetNamespace = "http://www.example.org/contract/DoubleIt",
        wsdlLocation = "classpath:DoubleIt.wsdl",
        portName = "DoubleItPort",
        serviceName = "DoubleItService",
        endpointInterface = "org.example.contract.doubleit.DoubleItPortType")
public class DoubleItPortTypeImpl implements DoubleItPortType {

    public int doubleIt(int numberToDouble) {
        return numberToDouble * 2;
    }
}
