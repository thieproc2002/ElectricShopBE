package iuh.edu.entity;

public class VerifyPasswordRequest {
    private Long userId;
    private String oldPassword;

    // Constructors
    public VerifyPasswordRequest() {
    }

    public VerifyPasswordRequest(Long userId, String oldPassword) {
        this.userId = userId;
        this.oldPassword = oldPassword;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
