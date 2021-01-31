package com.ppk.myapp.api.request;

public class RegisterRequest {
    private String username;
    private String email;
    private String name;
    private String password;
    private String pass_confirm;

    // builder design pattern
    public static class Builder {
        private String username;
        private String email;
        private String name;
        private String password;
        private String pass_confirm;

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

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setPass_confirm(String pass_confirm) {
            this.pass_confirm = pass_confirm;
            return this;
        }

        public RegisterRequest build(){
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.username = this.username;
            registerRequest.email = this.email;
            registerRequest.name = this.name;
            registerRequest.password = this.password;
            registerRequest.pass_confirm = this.pass_confirm;

            return registerRequest;
        }
    }

    private RegisterRequest(){

    }
}
