package learning;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jeongjin Kim
 * @since 2018. 9. 12.
 */
public class TravelSelfRefObjectTest {
    SampleObject o1 = new SampleObject("o1", null);
    SampleObject o2 = new SampleObject("o2", o1);
    SampleObject o3 = new SampleObject("o3", o2);
    SampleObject o4 = new SampleObject("o4", o1);
    SampleObject o5 = new SampleObject("o5", o2);
    SampleObject o6 = new SampleObject("o6", null);
    SampleObject o7 = new SampleObject("o7", o2);
    SampleObject o8 = new SampleObject("o8", o7);
    SampleObject o9 = new SampleObject("o9", o8);
    SampleObject oa = new SampleObject("oa", o7);

    @Test
    public void travel() {
        Set<SampleObject> sampleObjects = new HashSet<>();
        Map<String, Integer> visited = new HashMap<>();

        sampleObjects.add(o1);
        sampleObjects.add(o2);
        sampleObjects.add(o3);
        sampleObjects.add(o4);
        sampleObjects.add(o5);
        sampleObjects.add(o7);
        sampleObjects.add(o8);
        sampleObjects.add(oa);

        long start = System.nanoTime();
        sampleObjects = visit3(sampleObjects);
        long end = System.nanoTime();
        System.out.println(end - start);
        System.out.println(sampleObjects.size());

    }

    private Set<SampleObject> visit3(Set<SampleObject> sampleObjects) {
        System.out.println(sampleObjects.size());


        if(sampleObjects.size() == 0) return sampleObjects;

        Set<SampleObject> temp = new HashSet<>();

        for (SampleObject sampleObject : sampleObjects) {
            System.out.println(sampleObject.toString());
            if (sampleObject.getParentObj() == null || sampleObjects.contains(sampleObject.getParentObj())) {
            } else {
                temp.add(sampleObject.getParentObj());
            }
        }

        sampleObjects.addAll(visit3(temp));
        return sampleObjects;
    }
}

class SampleObject {
    String id;
    SampleObject parentObj;

    @Override
    public String toString() {
        return "SampleObject{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleObject that = (SampleObject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SampleObject getParentObj() {
        return parentObj;
    }

    public void setParentObj(SampleObject parentObj) {
        this.parentObj = parentObj;
    }

    public SampleObject(String id, SampleObject parentObj) {
        this.id = id;
        this.parentObj = parentObj;
    }
}