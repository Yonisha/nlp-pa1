package common;

import java.util.*;

public class Commons {
    public static String join(String[] s, String delimiter) {
        List<String> collection = new ArrayList<String>(Arrays.asList(s));

        return join(collection, delimiter);
    }

    public static String join(Collection<?> s, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }

        return builder.toString();
    }
}
