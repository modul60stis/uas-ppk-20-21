package com.ppk.myapp.api.request;

public class UpdateRequest {
    private String username;
    private String email;
    private String name;

    // builder design pattern
    public static class Builder {
        private String username;
        private String email;
        private String name;

        public Builder(String username) {
            this.username = username;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public UpdateRequest build(){
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.username = this.username;
            updateRequest.email = this.email;
            updateRequest.name = this.name;

            return updateRequest;
        }
    }

    private UpdateRequest(){ }
}
