package com.wheredidmymoneygo.service;

import com.wheredidmymoneygo.dto.Category;
import com.wheredidmymoneygo.dto.CategoryDetail;
import com.wheredidmymoneygo.dto.Expense;
import com.wheredidmymoneygo.dto.ExpenseStatitic;
import com.wheredidmymoneygo.model.CategoryWrapper;
import com.wheredidmymoneygo.model.ExpenseModel;
import com.wheredidmymoneygo.model.WayMoneySpentEnum;
import com.wheredidmymoneygo.repository.CategoryRepository;
import com.wheredidmymoneygo.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpenseService {

    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;

    public ResponseEntity saveExpense(Expense expense) {
        ExpenseModel expenseModel = new ExpenseModel();
        expenseModel.setValue(expense.getValue());

        try {
            buildCategoryWrapper(expense, expenseModel);
        } catch (Exception e) {
            System.err.println("Category doesn't exists. Needs to be created first. Category: " + expense.getCategory());
            return new ResponseEntity("Category doesn't exist", HttpStatus.BAD_REQUEST);
        }

        try {
            WayMoneySpentEnum wayMoneySpentEnum = WayMoneySpentEnum.valueOf(expense.getWayMoneySpent());
            expenseModel.setWayMoneySpent(wayMoneySpentEnum);
        } catch (IllegalArgumentException e) {
            System.err.println("Way Money Spend value not in enum: " + expense.getWayMoneySpent());
            return new ResponseEntity("Way money spend is not defined. Use paypal, bar or card", HttpStatus.BAD_REQUEST);
        }
        expenseModel.setTimestamp(Instant.now());

        expenseRepository.insert(expenseModel);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    private void buildCategoryWrapper(Expense expense, ExpenseModel expenseModel) throws Exception {
        CategoryWrapper categoryWrapper = new CategoryWrapper();
        Optional<Category> category = categoryRepository.findByName(expense.getCategory());
        category.ifPresent(cat -> {
            categoryWrapper.setName(cat.getName());
            categoryWrapper.setId(cat.getId());
        });
        if (!category.isPresent()) {
            throw new Exception("Category doesn't exist. Create Category first");
        }
        expenseModel.setCategoryWrapper(categoryWrapper);
    }

    public List<ExpenseModel> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public ExpenseStatitic getExpensesStatitic(Instant from, Instant till) {
        ExpenseStatitic expenseStatitic = new ExpenseStatitic();
        expenseStatitic.setCategoryDetails(new ArrayList<>());
        double total = 0d;

        List<ExpenseModel> expenses = expenseRepository.findByTimestampBetween(from, till);

        for (ExpenseModel model : expenses) {
            total += model.getValue();
            boolean categoryFound = false;
            for (CategoryDetail category : expenseStatitic.getCategoryDetails()) {
                if (category.getCategoryName().equals(model.getCategoryWrapper().getName())) {
                    categoryFound = true;
                    double totalCategoryValue = category.getValue();
                    category.setValue(model.getValue() + totalCategoryValue);
                }
            }
            if (!categoryFound) {
                CategoryDetail category = new CategoryDetail();
                category.setCategoryName(model.getCategoryWrapper().getName());
                category.setValue(model.getValue());
                expenseStatitic.getCategoryDetails().add(category);
            }
        }
        expenseStatitic.setTotal(total);
        for (CategoryDetail category : expenseStatitic.getCategoryDetails()) {
            category.setPercentageOfTotal(category.getValue() * 100 / total);
        }
        expenseStatitic.setAllExpenses(getExpensesInTimespan(from, till));
        return expenseStatitic;
    }

    public List<Expense> getExpensesInTimespan(Instant from, Instant till) {
        List<ExpenseModel> expenseModels = expenseRepository.findByTimestampBetween(from, till);
        return expenseModels.stream().map(expenseModel -> {
            Expense expense = new Expense();
            expense.setCategory(expenseModel.getCategoryWrapper().getName());
            expense.setValue(expenseModel.getValue());
            expense.setWayMoneySpent(expenseModel.getWayMoneySpent().name());
            return expense;
        }).collect(Collectors.toList());
    }

    public List<ExpenseModel> getExpensesModelInTimespan(Instant from, Instant till) {
        return expenseRepository.findByTimestampBetween(from, till);
    }
}
