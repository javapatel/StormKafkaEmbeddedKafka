package com.storm;

import java.util.List;

public interface PaymentStoreService {
    static PaymentStoreService instance = new InMemoryPaymentStore();

    void store(Payment payment);

    List<Payment> getPayment(String paymentId);

    static PaymentStoreService getDefaultInstance() {
        return instance;
    }
}
