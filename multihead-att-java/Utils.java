import java.util.Iterator;
import java.util.TreeMap;

public final class Utils {
    public static int[] topIndexes(double[] values, int topk) {
        int[] indexes = new int[topk];
        TreeMap<Double, Integer> map = new TreeMap<Double, Integer>();
        for (int i = 0; i < values.length; ++i) {
            map.put(-values[i], i);
        }
        int cnt = 0;
        Iterator<Integer> iter = map.values().iterator();
        while (iter.hasNext() && cnt < topk) {
            indexes[cnt] = (int) iter.next();
            cnt += 1;
        }
        return indexes;
    }

    public static void main(String[] args) {
        topIndexes(new double[]{1., 2., 3., 2.5, 2.1}, 3);
    }

    public static String extractFilePrefix(String path) {
        String[] paths = path.trim().split("/");
        String filename = paths[paths.length - 1];
        int tmp = filename.indexOf(".attention");
        return filename.substring(0, tmp);
    }
}
