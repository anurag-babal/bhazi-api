package com.example.bhazi.core.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;

@Getter
public class ResponseDto extends RepresentationModel<ResponseDto> {
    private final int status;
    private final int count;
    private final Object data;
    private final String message;

    public ResponseDto(Builder builder) {
        this.status = builder.status;
        this.count = builder.count;
        this.data = builder.data;
        this.message = builder.message;
    }

    @Override
    public String toString() {
        return "Response [status=" + status + ", count=" + count + ", data=" + data + ", message=" + message + "]";
    }

    public static class Builder {
        private int status;
        private int count;
        private Object data;
        private String message;

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseDto build() {
            return new ResponseDto(this);
        }
    }
}
