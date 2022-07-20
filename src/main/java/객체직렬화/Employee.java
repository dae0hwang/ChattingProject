package 객체직렬화;

import java.io.Serializable;

public class Employee implements Serializable {
    String name;
    String addr;
    String jumin;
    String phone;

    public Employee(String name, String addr, String jumin, String phone) {
        this.name = name;
        this.addr = addr;
        this.jumin = jumin;
        this.phone = phone;

    }
}
