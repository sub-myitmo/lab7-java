package common.actions;

import java.io.Serializable;

public class Response implements Serializable {
    private String response;
    private ResponseCode isGoodResponse;

    public Response(ResponseCode isGoodResponse, String response) {
        this.isGoodResponse = isGoodResponse;
        this.response = response;
    }

    /**
     * @return Response body.
     */
    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "UserResponse[" + response+ "]";
    }

    public ResponseCode getIsGoodResponse() {
        return isGoodResponse;
    }
}
