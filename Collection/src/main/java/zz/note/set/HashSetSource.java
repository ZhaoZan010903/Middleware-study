package zz.note.set;

import java.util.HashSet;

public class HashSetSource {
    public static void main(String[] args) {

        HashSet hashSet = new HashSet();
        hashSet.add("Java");
        hashSet.add("GO");
        hashSet.add("Java");

        /**
         * HashSet 的源码解读
         * 1. 执行构造器 HashSet()
         *    public HashSet() {
         *         map = new HashMap<>();
         *     }
         *
         * 2. 执行 add()
         *   public boolean add(E e) {
         *         return map.put(e, PRESENT)==null;   // (static) PRESENT = new Object();
         *     }
         *
         *  3. 执行 put() , 该方法会执行 hash(key) 得到key对应的hash值 算法 h = key.hashCode() ^ (h >>> 16)
         *      public V put(K key, V value) {     // key = 是存入的东西
         *         return putVal(hash(key), key, value, false, true);
         *     }
         *
         *  4. 执行 putVal()
         *      final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
         *                    boolean evict) {
         *         Node<K,V>[] tab; Node<K,V> p; int n, i;  // 定义了辅助变量
         *         // table 就是 HashMap 的一个数组,类型是 Node[]
         *         if ((tab = table) == null || (n = tab.length) == 0)
         *             n = (tab = resize()).length;
         *         if ((p = tab[i = (n - 1) & hash]) == null)
         *             tab[i] = newNode(hash, key, value, null);
         *         else {
         *             Node<K,V> e; K k;
         *             if (p.hash == hash &&
         *                 ((k = p.key) == key || (key != null && key.equals(k))))
         *                 e = p;
         *             else if (p instanceof TreeNode)
         *                 e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
         *             else {
         *                 for (int binCount = 0; ; ++binCount) {
         *                     if ((e = p.next) == null) {
         *                         p.next = newNode(hash, key, value, null);
         *                         if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
         *                             treeifyBin(tab, hash);
         *                         break;
         *                     }
         *                     if (e.hash == hash &&
         *                         ((k = e.key) == key || (key != null && key.equals(k))))
         *                         break;
         *                     p = e;
         *                 }
         *             }
         *             if (e != null) { // existing mapping for key
         *                 V oldValue = e.value;
         *                 if (!onlyIfAbsent || oldValue == null)
         *                     e.value = value;
         *                 afterNodeAccess(e);
         *                 return oldValue;
         *             }
         *         }
         *         ++modCount;
         *         if (++size > threshold)
         *             resize();
         *         afterNodeInsertion(evict);
         *         return null;
         *     }
         */
    }
}
