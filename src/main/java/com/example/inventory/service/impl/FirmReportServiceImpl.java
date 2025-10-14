package com.example.inventory.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.inventory.dto.firm.FirmBalanceResponse;
import com.example.inventory.entity.Firm;
import com.example.inventory.exception.BusinessException;
import com.example.inventory.repository.FirmRepository;
import com.example.inventory.repository.PaymentRepository;
import com.example.inventory.repository.SaleRepository;
import com.example.inventory.service.FirmReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirmReportServiceImpl implements FirmReportService {

    private final FirmRepository firmRepository;
    private final SaleRepository saleRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<FirmBalanceResponse> getAllFirmBalances() {
        List<Firm> firms = firmRepository.findAll();

        return firms.stream().map(firm -> {
            BigDecimal totalSales = saleRepository.sumTotalAmountByFirmId(firm.getId())
                    .orElse(BigDecimal.ZERO);

            BigDecimal totalPayments = paymentRepository.sumPaidAmountByFirmId(firm.getId())
                    .orElse(BigDecimal.ZERO);

            BigDecimal balance = totalSales.subtract(totalPayments);

            return FirmBalanceResponse.builder()
                    .firmId(firm.getId())
                    .firmName(firm.getName())
                    .totalSales(totalSales)
                    .totalPayments(totalPayments)
                    .balance(balance)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
	public byte[] exportFirmBalancesToExcel() {
        try {
            List<FirmBalanceResponse> balances = getAllFirmBalances();

            try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.createSheet("Firm Balances");

                // Başlık satırı
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Firma ID", "Firma Adı", "Toplam Satış", "Toplam Ödeme", "Bakiye"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }

                // Verileri doldur
                int rowIdx = 1;
                for (FirmBalanceResponse balance : balances) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(balance.getFirmId());
                    row.createCell(1).setCellValue(balance.getFirmName());
                    row.createCell(2).setCellValue(balance.getTotalSales().doubleValue());
                    row.createCell(3).setCellValue(balance.getTotalPayments().doubleValue());
                    row.createCell(4).setCellValue(balance.getBalance().doubleValue());
                }

                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(out);
                return out.toByteArray();
            }

        } catch (Exception e) {
        	throw new BusinessException("Rapor oluşturulurken bir hata meydana geldi.", e);
        }
    }

}
