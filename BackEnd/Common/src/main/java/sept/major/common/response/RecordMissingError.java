package sept.major.common.response;

public class RecordMissingError extends ResponseError {
    public RecordMissingError(String field) {
        super(field, "No record found for this key");
    }
}
