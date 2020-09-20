package sept.major.users;

import sept.major.users.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

public abstract class UserTestHelper {
    /**
     * Generates a random string with numbers and letters (both upper case and lower case)
     *
     * @param length The length of the string to generate
     * @return The string generated
     */
    public static String randomAlphanumericString(int length) {
        final int[] uppercaseRange = {65, 91}; // The ASCII range for upper case letters
        final int[] lowerCaseRange = {97, 123}; // The ASCII range for lower case letters
        final int[] numbersRange = {48, 58}; // The ASCII range for numbers

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rangeToUse = (int) ((Math.random() * (4 - 1) + 1)); // Generates number between 1-3
            int charToUse;
            if (rangeToUse == 1) {
                // Get random number in the range
                charToUse = (int) ((Math.random() * (uppercaseRange[0] - uppercaseRange[1]) + uppercaseRange[1]));
            } else if (rangeToUse == 2) {
                // Get random number in the range
                charToUse = (int) ((Math.random() * (lowerCaseRange[0] - lowerCaseRange[1]) + lowerCaseRange[1]));
            } else {
                // Get random number in the range
                charToUse = (int) ((Math.random() * (numbersRange[0] - numbersRange[1]) + numbersRange[1]));
            }
            stringBuilder.append((char) charToUse);
        }

        return stringBuilder.toString();
    }

    /**
     * Generates with map randomly with the values to represent a {@link UserEntity}
     *
     * @return The randomly generated map
     */
    public static Map<String, String> randomEntityMap() {
        return new HashMap<String, String>() {{
            put("username", randomAlphanumericString(20));
            put("password", randomAlphanumericString(20));
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};
    }


    /**
     * Generates with map randomly with the values to represent a {@link UserEntity} with username being the provided username
     *
     * @param username The entity's username
     * @return The generated map
     */
    public static Map<String, String> randomEntityMap(String username) {
        Map<String, String> entityMap = randomEntityMap();
        entityMap.put("username", username);
        return entityMap;
    }

    /**
     * Generates with map randomly with the values to represent a {@link UserEntity} with username being the provided username
     * and userType being the provided userType
     *
     * @param username The entity's username
     * @param userType The entitiy's userType
     * @return The generated map
     */
    public static Map<String, String> randomEntityMap(String username, String userType) {
        Map<String, String> entityMap = randomEntityMap(username);
        entityMap.put("userType", userType);
        return entityMap;
    }


    /**
     * Creates a {@link UserEntity} based on the values in the provided HashMap
     *
     * @param entityMap The {@link HashMap} to create the {@link UserEntity} from
     * @return The {@link UserEntity} created from the {@link HashMap}
     */
    public static UserEntity createUserEntity(Map<String, String> entityMap) {
        return new UserEntity(
                entityMap.get("username"),
                entityMap.get("password"),
                entityMap.get("userType"),
                entityMap.get("name"),
                entityMap.get("phone"),
                entityMap.get("address")
        );
    }


    /**
     * Convenience method for created a random {@link UserEntity}
     *
     * @return The random {@link UserEntity}
     */
    public static UserEntity randomEntity() {
        return createUserEntity(randomEntityMap());
    }


    /**
     * Convenience method for created a random {@link UserEntity} with userId being the provided
     *
     * @param username The value of the username
     * @return The random {@link UserEntity}
     */
    public static UserEntity randomEntity(String username) {
        return createUserEntity(randomEntityMap(username));
    }

    /**
     * Updates the provided entity with the values provided
     *
     * @param existing    The entity to update
     * @param patchValues The values to update the entity with
     * @return The updated entity
     */
    public static UserEntity patchEntity(UserEntity existing, HashMap<String, String> patchValues) {

        UserEntity newEntity = new UserEntity(
                existing.getUsername(),
                existing.getPassword(),
                existing.getName(),
                existing.getName(),
                existing.getPhone(),
                existing.getAddress()
        );

        if (patchValues.get("username") != null) {
            newEntity.setUsername(patchValues.get("username"));
        }

        if (patchValues.get("password") != null) {
            newEntity.setPassword(patchValues.get("password"));
        }

        if (patchValues.get("userType") != null) {
            newEntity.setUserType(patchValues.get("userType"));
        }

        if (patchValues.get("name") != null) {
            newEntity.setName(patchValues.get("name"));
        }

        if (patchValues.get("phone") != null) {
            newEntity.setPhone(patchValues.get("phone"));
        }

        if (patchValues.get("address") != null) {
            newEntity.setAddress(patchValues.get("address"));
        }

        return newEntity;
    }
}
