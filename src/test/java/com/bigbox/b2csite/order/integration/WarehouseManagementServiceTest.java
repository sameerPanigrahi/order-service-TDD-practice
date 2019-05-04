package com.bigbox.b2csite.order.integration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(value = PowerMockRunner.class)
@PrepareForTest(value = WarehouseManagementServiceTest.class)
public class WarehouseManagementServiceTest {

	@Test
	@Ignore
	public void test_ConfirmShipmentInternal_success() {

		/*
		 * // Setup PowerMockito.mockStatic(WarehouseManagementService.class); try {
		 * PowerMockito .doThrow(new WMSUnavailableException("mock exception"))
		 * .when(WarehouseManagementService.class,
		 * PowerMockito.method(WarehouseManagementService.class,
		 * "confirmShipment_internal"))
		 * .withArguments(Matchers.any(OrderMessage.class)); } catch (Exception e1) { //
		 * do nothing }
		 * 
		 * // Execution PowerMockito.verifyStatic(); try {
		 * WarehouseManagementService.confirmShipment(new OrderMessage()); } catch
		 * (WMSUnavailableException e) { // do nothing }
		 * 
		 * // Verification
		 */

		/*
		 * PowerMockito.mockStatic(WarehouseManagementService.class);
		 * 
		 * PowerMockito.when(WarehouseManagementService.confirmShipment(Matchers.any(
		 * OrderMessage.class))).thenReturn(1);
		 * PowerMockito.verifyStatic(WarehouseManagementService.confirmShipment(
		 * orderMessage)
		 */

	}
}
