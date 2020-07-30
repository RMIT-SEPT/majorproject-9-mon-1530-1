package sept.major.users.response.error;

public class RecordMissingError extends ResponseError {
    public RecordMissingError(String field) {
        super(field, "No record found for this key");
    }
}
