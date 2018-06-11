package com.egs.account.model.ajax;

/**
 * @author Hayk_Mkhitaryan
 */
public class JsonResponse {

	private String status;

    private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
	}

    public JsonResponse() {
    }

    public JsonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
