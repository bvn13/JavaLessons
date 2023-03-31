import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

public class TestJavaCollections {
    @Test
    void test1() {
        new ArrayList<Integer>();
        new LinkedList<Integer>();

        List<Integer> a = new ArrayList<Integer>();
        a.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        a.removeAll(a.subList(2, 4));
        System.out.println(a);

        Map<String, String> map = new HashMap<String, String>();
        TreeSet<Integer> set = new TreeSet<Integer>();
        set.addAll(Arrays.asList(9,4,5,7,2,3));
        System.out.println(set);

        Queue<Integer> dequeue = new ArrayDeque<>();
        Queue<Integer> queue = new LinkedList<>();

    }
}
