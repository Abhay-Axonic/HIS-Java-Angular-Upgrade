package com.param.hl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

public class SendHl7AMessage {

   private static final int PORT_NUMBER = 52463;
    private static final String HOST = "219.91.153.48";
   private static HapiContext context = new DefaultHapiContext();
   public static void main(String[] args) throws Exception {


      boolean useTls = false;

      String msg = "MSH|^~\\&|HMIS|Hospitals|GE ICU|1|20220308230503||ADT^A02^ADT_A02|123420220308230503|P|2.5|||AL|NE|EN\n" +
              "EVN|A02|20220308230503\n" +
              "PID||12680|UHID-1276|UHID-1276|^Kiara||19990307|F|||^114038^^^^India|001||||||||||||N\n" +
              "PV1||Class - 1|^GEN^station|Medical Admission||||||M|||||||||ADM-83|||||||||||||||||||||||||20220307060300\n" +
              "PV2|||Normal||||||20220307060300\n";

      Parser p = context.getPipeParser();
      Message adt = p.parse(msg);
      Connection connection = context.newClient(HOST, PORT_NUMBER, useTls);
      Initiator initiator = connection.getInitiator();
      Message response = initiator.sendAndReceive(adt);
      String responseString = p.encode(response);
      connection.close();
   }
}
