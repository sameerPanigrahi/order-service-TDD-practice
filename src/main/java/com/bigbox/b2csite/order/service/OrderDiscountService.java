package com.bigbox.b2csite.order.service;

import com.bigbox.b2csite.order.model.entity.OrderEntity;

public interface OrderDiscountService {
	OrderEntity setDiscount(OrderEntity orderEntity);
}