package sept.major.hours.blackbox;

import sept.major.common.testing.BlackboxTestHelper;

public class HoursBlackBoxHelper extends BlackboxTestHelper {

    @Override
    public String getInitScriptName() {
        return "hour.sql";
    }

    @Override
    public String getApiExtension() {
        return "hours";
    }
}
