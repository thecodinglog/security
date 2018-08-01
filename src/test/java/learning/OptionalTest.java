package learning;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

/**
 * @author Jeongjin Kim
 * @since 2018. 7. 31.
 */

public class OptionalTest {

    class House{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    class Building{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    @Ignore("Learning test")
    public void optional(){
        House house = null;
        Optional<House> houseOpt = Optional.ofNullable(house);
        Optional<Building> buildingOpt = houseOpt.map(house1 -> new Building());

        Building b1 = buildingOpt.orElse(null);
    }
}
