package gamesys.messaging.serialization;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class MessageSerializerUTest {


    @Test
    public void testGetJson() throws Exception {
        TestObject panos = new TestObject("panos", 12);
        String json = MessageSerializer.getJson(panos);

        assertThat(json).isEqualTo("{\"name\":\"panos\",\"age\":12}");

    }

    @Test
    public void testGetObject() throws Exception {
        TestObject panos = MessageSerializer.getObject("{\"name\":\"panos\",\"age\":12}", TestObject.class);

        assertThat(panos.getName()).isEqualTo("panos");
        assertThat(panos.getAge()).isEqualTo(12);
    }




    private static class TestObject {
        private String name;
        private int age;

        public TestObject() {
        }

        public TestObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}
