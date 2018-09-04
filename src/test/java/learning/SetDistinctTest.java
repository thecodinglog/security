package learning;

import cothe.security.core.domain.Permission;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Jeongjin Kim
 * @since 2018. 8. 29.
 */
public class SetDistinctTest {
    @Test
    public void addSameIdItem(){
        Set<Permission> permissions = new HashSet<>();
        Permission p1 = new Permission("id1",null,null,null);
        Permission p2 = new Permission("id1",null,null,null);

        permissions.add(p1);
        permissions.add(p2);

        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());

        assertEquals(1, permissions.size());
    }

    @Test
    public void addDifferentIdItem(){
        Set<Permission> permissions = new HashSet<>();
        Permission p1 = new Permission("id1",null,null,null);
        Permission p2 = new Permission("id2",null,null,null);

        permissions.add(p1);
        permissions.add(p2);

        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());

        assertEquals(2, permissions.size());
    }

    @Test
    public void addAllItems(){
        List<Permission> permissionsList = new ArrayList<>();
        permissionsList.add(new Permission("id2",null,null,null));
        permissionsList.add(new Permission("id3",null,null,null));

        Set<Permission> permissions = new HashSet<>();
        Permission p1 = new Permission("id1",null,null,null);
        Permission p2 = new Permission("id2",null,null,null);

        permissions.add(p1);
        permissions.add(p2);

        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());

        assertEquals(2, permissions.size());

        permissions.addAll(permissionsList);
        assertEquals(3, permissions.size());
    }
}
