package pfds.immutable;

import java.util.ArrayList;
import java.util.List;

class Utils {

    public static <T> List<T> arrayListOf(T elem) {
        List<T> l = new ArrayList<T>();
        l.add(elem);
        return l;
    }

    public static long pow(long a, int b) {
        if (b==0) {
            return 1;
        }
        if (b % 2 == 0) {
            return pow(a*a, b/2);
        }
        return a * pow(a*a, (b-1)/2);
    }
}