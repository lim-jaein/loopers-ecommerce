package com.loopers.interfaces.api.point;

public class PointV1Dto {
    public record PointChargeRequest(int amount) { }

    public record PointResponse(int balance) {
        public static PointResponse from(int balance) {
            return new PointResponse(balance);
        }
    }
}
