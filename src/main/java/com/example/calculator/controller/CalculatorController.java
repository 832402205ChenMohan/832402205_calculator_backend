package com.example.calculator.controller;

import com.example.calculator.model.HistoryRecord;
import com.example.calculator.service.CalculatorService;
import com.example.calculator.service.ExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody Map<String, String> body) {
        String expression = body.getOrDefault("expression", "");
        try {
            return ResponseEntity.ok(calculatorService.calculate(expression));
        } catch (ExpressionParser.CalcException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Internal server error"));
        }
    }

    @GetMapping("/history")
    public Map<String, Object> getHistory() {
        List<HistoryRecord> records = calculatorService.listHistory();
        return Map.of("success", true, "records", records);
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deleteHistory(@PathVariable Long id) {
        if (calculatorService.deleteRecord(id)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Record deleted"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "Record not found"));
    }

    @DeleteMapping("/history")
    public Map<String, Object> clearHistory() {
        calculatorService.clearAll();
        return Map.of("success", true, "message", "All history cleared");
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
