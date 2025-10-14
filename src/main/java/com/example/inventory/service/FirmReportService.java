package com.example.inventory.service;

import java.util.List;

import com.example.inventory.dto.firm.FirmBalanceResponse;

public interface FirmReportService {

	   List<FirmBalanceResponse> getAllFirmBalances();

	   byte[] exportFirmBalancesToExcel();
}
