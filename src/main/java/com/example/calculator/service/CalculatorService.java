package com.example.calculator.service;

import com.example.calculator.model.HistoryRecord;
import com.example.calculator.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CalculatorService {

    private final HistoryRepository historyRepository;

    public CalculatorService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public Map<String, Object> calculate(String expression) {
        Object result = ExpressionParser.calculate(expression);
        HistoryRecord record = historyRepository.save(new HistoryRecord(expression, String.valueOf(result)));
        return Map.of(
            "success", true,
            "expression", expression,
            "result", result,
            "id", record.getId()
        );
    }

    public List<HistoryRecord> listHistory() {
        return historyRepository.findAllByOrderByIdDesc();
    }

    public boolean deleteRecord(Long id) {
        if (historyRepository.existsById(id)) {
            historyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void clearAll() {
        historyRepository.deleteAll();
    }
}
