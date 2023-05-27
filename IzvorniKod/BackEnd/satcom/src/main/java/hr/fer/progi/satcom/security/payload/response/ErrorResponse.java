package hr.fer.progi.satcom.security.payload.response;

/**
 * The type Error response.
 *
 * @author satcomBackend
 */
public class ErrorResponse {

    private String error;

    /**
     * Instantiates a new Error response.
     *
     * @param error the error
     */
    public ErrorResponse(String error) {
        this.error = error;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }
}
