package sept.major.users;

import java.util.HashMap;
import java.util.Map;

public abstract class UserServiceTestHelper {
    public static String randomAlphanumericString(int length) {
        final int[] uppercaseRange = {65, 91};
        final int[] lowerCaseRange = {97, 123};
        final int[] numbersRange = {48, 58};

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rangeToUse = (int) ((Math.random() * (4 - 1) + 1));
            int charToUse;
            if (rangeToUse == 1) {
                charToUse = (int) ((Math.random() * (uppercaseRange[0] - uppercaseRange[1]) + uppercaseRange[1]));
            } else if (rangeToUse == 2) {
                charToUse = (int) ((Math.random() * (lowerCaseRange[0] - lowerCaseRange[1]) + lowerCaseRange[1]));
            } else {
                charToUse = (int) ((Math.random() * (numbersRange[0] - numbersRange[1]) + numbersRange[1]));
            }
            stringBuilder.append((char) charToUse);
        }

        return stringBuilder.toString();
    }

    public static Map<String, String> randomEntityMap() {
        HashMap<String, String> entityMap = new HashMap<>();
        entityMap.put("username", randomAlphanumericString(20));
        entityMap.put("password", randomAlphanumericString(20));
        entityMap.put("userType", randomAlphanumericString(20));
        entityMap.put("name", randomAlphanumericString(20));
        entityMap.put("phone", randomAlphanumericString(20));
        entityMap.put("address", randomAlphanumericString(20));
        return entityMap;
    }

    public static Map<String, String> randomEntityMap(String password) {
        HashMap<String, String> entityMap = new HashMap<>();
        entityMap.put("username", randomAlphanumericString(20));
        entityMap.put("password", password);
        entityMap.put("userType", randomAlphanumericString(20));
        entityMap.put("name", randomAlphanumericString(20));
        entityMap.put("phone", randomAlphanumericString(20));
        entityMap.put("address", randomAlphanumericString(20));
        return entityMap;
    }
}
