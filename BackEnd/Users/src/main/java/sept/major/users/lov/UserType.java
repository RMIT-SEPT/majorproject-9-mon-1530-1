package sept.major.users.lov;

import lombok.Getter;

public enum UserType {
    CUSTOMER("customer"), WORKER("worker"), ADMIN("admin");

    @Getter
    String value;

    UserType(String mappedValue) {
        value = mappedValue;
    }
}
