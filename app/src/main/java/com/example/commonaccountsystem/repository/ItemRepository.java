package com.example.commonaccountsystem.repository;

import android.content.Context;

import com.example.commonaccountsystem.dao.AppDatabase;
import com.example.commonaccountsystem.dao.ItemDAO;
import com.example.commonaccountsystem.entity.Item;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemRepository {
    private static ItemRepository instance;
    private ItemDAO dao;
    private List<Item> items;
    private LocalDate now;
    private final List<String> TARGET_ITEM_NAMES_FOR_TOTAL_AMOUNT = Arrays.asList("食費", "日用品代", "旅費");

    private ItemRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        this.dao = db.itemDAO();
        now = LocalDate.now();
    }

    public static ItemRepository getInstance(Context context){
        if(instance == null){
            instance = new ItemRepository(context);
        }
        return instance;
    }

    public List<Item> fetchAll(){
        if(this.items == null){
            this.items = dao.selectAll();
        }
        return this.items;
    }

    public List<String> fetchNamesWithVariableCost(){
        fetchAll();
        return this.items.stream()
                // TODO: 自動登録機能を実装する場合は以下をコメントアウト
                //.filter(i -> i.payment_date == null)
                .sorted((i1,i2) -> Integer.compare(i1.displayOrder, i2.displayOrder))
                .map(i -> i.name)
                .collect(Collectors.toList());
    }

    public int fetchIdByName(String name){
        if(this.items == null){
            return dao.selectIdByName(name);
        }
        Optional<Integer> id = items.stream().filter(i -> i.name.equals(name)).map(i -> i.id).findFirst();
        return id.orElse(-1);
    }

    public int fetchCostByName(String name){
        fetchAll();
        int id = fetchIdByName(name);
        Optional<Integer> cost = items.stream().filter(i -> i.id == id).map(i -> i.cost).findFirst();
        return cost.orElse(0);
    }
    public LocalDate fetchPaymentDateByName(String name){
        fetchAll();
        int id = fetchIdByName(name);
        Optional<String> paymentDayStr = items.stream().filter(i -> i.id == id).map(i -> i.paymentDay).filter(day -> day != null).findFirst();
        Optional<Integer> paymentDay = paymentDayStr.map(Integer::parseInt);
        return LocalDate.of(now.getYear(), now.getMonth(), paymentDay.orElse(now.getDayOfMonth()));
    }

    public int fetchPayerIdByName(String name){
        fetchAll();
        int id = fetchIdByName(name);
        Optional<Integer> payerId = items.stream().filter(i -> i.id == id).map(i -> i.defaultPayer).findFirst();
        return payerId.orElse(-1);
    }

    public List<Item> fetchTargetItemsForTotalAmount(){
        fetchAll();
        return items.stream()
                .filter(i -> TARGET_ITEM_NAMES_FOR_TOTAL_AMOUNT.contains(i.name))
                .sorted((i1,i2) -> Integer.compare(i1.displayOrder, i2.displayOrder))
                .collect(Collectors.toList());
    }
}
