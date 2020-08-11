package syncer.syncerreplication.util.type;

import syncer.syncerreplication.util.objectUtils.Iterators;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author zhanenqiang
 * @Description 描述
 * @Date 2020/4/8
 */
public class SyncerTuple3<T1, T2, T3> implements Iterable<Object>, Serializable {
    private static final long serialVersionUID = 1L;
    private T1 v1;
    private T2 v2;
    private T3 v3;

    public SyncerTuple3() {
    }

    public SyncerTuple3(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public SyncerTuple3(SyncerTuple3<T1, T2, T3> rhs) {
        this.v1 = rhs.getV1();
        this.v2 = rhs.getV2();
        this.v3 = rhs.getV3();
    }

    public T1 getV1() {
        return v1;
    }

    public T2 getV2() {
        return v2;
    }

    public T3 getV3() {
        return v3;
    }

    public void setV1(T1 v1) {
        this.v1 = v1;
    }

    public void setV2(T2 v2) {
        this.v2 = v2;
    }

    public void setV3(T3 v3) {
        this.v3 = v3;
    }

    public <V1, V2, V3> SyncerTuple3<V1, V2, V3> map(Function<SyncerTuple3<T1, T2, T3>, SyncerTuple3<V1, V2, V3>> function) {
        return function.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        SyncerTuple3<?, ?, ?> tuple3 = (SyncerTuple3<?, ?, ?>) o;

        if (v1 != null ? !v1.equals(tuple3.v1) : tuple3.v1 != null) {
            return false;
        }
        if (v2 != null ? !v2.equals(tuple3.v2) : tuple3.v2 != null){
            return false;
        }
        return v3 != null ? v3.equals(tuple3.v3) : tuple3.v3 == null;
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        result = 31 * result + (v3 != null ? v3.hashCode() : 0);
        return result;
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.iterator(getV1(), getV2(), getV3());
    }

    @Override
    public String toString() {
        return "[" + v1 + ", " + v2 + ", " + v3 + "]";
    }

    public static <V> SyncerTuple3<V, V, V> from(V... ary) {
        if (ary == null || ary.length != 3){
            throw new IllegalArgumentException();
        }
        return new SyncerTuple3<>(ary[0], ary[1], ary[2]);
    }

    public static <V> SyncerTuple3<V, V, V> from(Iterator<V> iterator) {
        List<V> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return from(list.toArray((V[]) new Object[list.size()]));
    }

    public static <V> SyncerTuple3<V, V, V> from(Iterable<V> iterable) {
        return from(iterable.iterator());
    }

    public static <V> SyncerTuple3<V, V, V> from(Collection<V> collection) {
        return from((Iterable<V>) collection);
    }

    public Object[] toArray() {
        return new Object[]{getV1(), getV2(), getV3()};
    }

    public <T> T[] toArray(Class<T> clazz) {
        T[] ary = (T[]) Array.newInstance(clazz, 3);
        if (!clazz.isInstance(getV1())) {
            throw new UnsupportedOperationException();
        }
        ary[0] = (T) getV1();
        if (!clazz.isInstance(getV2())) {
            throw new UnsupportedOperationException();
        }
        ary[1] = (T) getV2();
        if (!clazz.isInstance(getV3())) {
            throw new UnsupportedOperationException();
        }
        ary[2] = (T) getV3();
        return ary;
    }

    public <T> T toObject(Function<SyncerTuple3<T1, T2, T3>, T> func) {
        return func.apply(this);
    }
}
