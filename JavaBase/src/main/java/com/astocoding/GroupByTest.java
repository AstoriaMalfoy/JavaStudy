package com.astocoding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/14 15:07
 *
 */
public class GroupByTest {
    public static void main(String[] args) {
        List<ProvideItem> items = new ArrayList<>();
        items.add(new ProvideItem("123",null));
        items.add(new ProvideItem("123",null));
        items.add(new ProvideItem("123",""));
        items.add(new ProvideItem("123","22"));
        items.add(new ProvideItem("234","123"));
        items.add(new ProvideItem("234","345"));
        items.add(new ProvideItem("345",null));
        items.add(new ProvideItem("456",""));


        Map<String, List<ProvideItem>> collect = items.stream().collect(Collectors.groupingBy(ProvideItem::getSpu));
        for (String s : collect.keySet()) {
            System.out.println( s + "  " + collect.get(s));
            List<String> skulIst = collect.get(s).stream().map(ProvideItem::getSku).collect(Collectors.toList());
            System.out.println(skulIst);
            System.out.println("------");
        }

    }

    @Data
    @ToString
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ProvideItem{
        private String spu;
        private String sku;
    }
}
