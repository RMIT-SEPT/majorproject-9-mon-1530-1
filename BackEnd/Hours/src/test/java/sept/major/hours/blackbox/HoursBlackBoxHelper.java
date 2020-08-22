package sept.major.hours.blackbox;

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
