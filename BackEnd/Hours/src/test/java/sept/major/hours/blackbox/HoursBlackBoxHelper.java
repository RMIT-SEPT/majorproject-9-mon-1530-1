package sept.major.hours.blackbox;

import sept.major.common.testing.BlackboxTestHelper;

public abstract class HoursBlackBoxHelper extends BlackboxTestHelper {

    @Override
    public String getInitScriptName() {
        return "hour.sql";
    }

    @Override
    public String getApiExtension() {
        return "hours";
    }

    protected String randomAlphanumericString(int length) {
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
}
