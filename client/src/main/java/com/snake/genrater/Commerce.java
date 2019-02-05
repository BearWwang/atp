package com.snake.genrater;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.SortedSet;
import java.util.TreeSet;

public class Commerce {
    private final Genrater genrater;

    protected Commerce(Genrater genrater) {
        this.genrater = genrater;
    }

    public String color() {
        return genrater.fakeValuesService().resolve("color.name", this, genrater);
    }

    public String department() {
        int numberOfDepartments = Math.max(genrater.random().nextInt(4), 1);
        SortedSet<String> departments = new TreeSet<String>();
        while (departments.size() < numberOfDepartments) {
            departments.add(genrater.fakeValuesService().resolve("commerce.department", this, genrater));
        }
        if (departments.size() > 1) {
            String lastDepartment = departments.last();
            return StringUtils.join(departments.headSet(lastDepartment), ", ") + " & " + lastDepartment;
        } else {
            return departments.first();
        }
    }

    public String productName() {
        return StringUtils.join(new String[] { 
            genrater.fakeValuesService().resolve("commerce.product_name.adjective", this, genrater),
            genrater.fakeValuesService().resolve("commerce.product_name.material", this, genrater),
            genrater.fakeValuesService().resolve("commerce.product_name.product", this, genrater) }, " ");
    }

    public String material() {
        return genrater.fakeValuesService().resolve("commerce.product_name.material", this, genrater);
    }

    /**
     * Generate a random price between 0.00 and 100.00 
     */
    public String price() {
        return price(0, 100);
    }

    public String price(double min, double max) {
        double price =  min + (genrater.random().nextDouble() * (max - min));
        return new DecimalFormat("#0.00").format(price);
    }

    public String promotionCode() {
        return promotionCode(6);
    }

    public String promotionCode(int digits) {
        return StringUtils.join(genrater.resolve("commerce.promotion_code.adjective"),
                genrater.resolve("commerce.promotion_code.noun"),
                genrater.number().digits(digits));
    }
}
