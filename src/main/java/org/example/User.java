package org.example;

import java.time.LocalDateTime;

public class User {
    private Long chatId;
    private String username;
    private UserState state;
    private LocalDateTime joinDate;
    private boolean verified;

    public User(Long chatId, String username) {
        this.chatId = chatId;
        this.username = username;
        this.state = UserState.PENDING_VERIFICATION;
        this.joinDate = LocalDateTime.now();
        this.verified = false;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}


