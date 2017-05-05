package com.gentlehealthcare.mobilecare.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Zyy on 2017/3/16.
 * Email: zhaoyangyang@heren.com
 */

public class CollectionsTool {

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection collection) {
        return (null == collection || collection.isEmpty());
    }
    /**
     * 返回a+b的新List.
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        List<T> result = new ArrayList<T>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 返回a-b的新List.
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<T>(a);
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }
}
