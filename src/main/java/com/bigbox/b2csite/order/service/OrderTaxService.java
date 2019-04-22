package com.bigbox.b2csite.order.service;

import com.bigbox.b2csite.order.model.entity.OrderEntity;

public interface OrderTaxService {
	OrderEntity deductTax(OrderEntity orderEntity);
}
