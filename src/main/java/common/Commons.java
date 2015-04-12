package common;

import java.util.Collection;
import java.util.Iterator;

public class Commons {
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

    public static double getLogProb(double prob){
        if (prob == 0)
            return -Double.MAX_VALUE;

        return Math.log(prob);
    }
}
