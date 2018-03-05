package hut34.wallet.testinfra.mail;

import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.Address;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MailTestUtils {

    public static List<MimeMessageParser> filterMessages(List<MimeMessageParser> msgs, String to, String subject) {
        Stream<MimeMessageParser> stream = msgs.stream();

        if(to != null) {
            stream = stream.filter((msg) -> getToAddresses(msg).contains(to));
        }

        if(subject != null) {
            stream = stream.filter((msg) -> getSubject(msg).contains(subject));
        }

        return stream.collect(Collectors.toList());
    }

    private static String getSubject(MimeMessageParser msg) {
        try {
            return msg.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static  List<String> getToAddresses(MimeMessageParser msg) {
        try {
            return msg.getTo().stream().map(Address::toString).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
