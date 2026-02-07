package com.example.commonaccountsystem.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.commonaccountsystem.entity.Withdrawal;
import com.example.commonaccountsystem.entity.WithdrawalWithItemAndPayer;

import java.util.List;

@Dao
public interface WithdrawalDAO {
    @Insert
    public void insert(Withdrawal... withdrawals);

    @Query("SELECT p.name AS payerName, i.name AS itemName, w.id, w.item_id, w.payer_id, w.price, w.liquidation_date, w.comment " +
            "FROM withdrawal AS w " +
            "JOIN item AS i ON w.item_id = i.id " +
            "JOIN payer AS p ON w.payer_id = p.id " +
            "WHERE date(w.liquidation_date) BETWEEN :yearPrevMonth ||'-22' AND :yearMonth ||'-21'" +
            "ORDER BY w.liquidation_date;")
    public List<WithdrawalWithItemAndPayer> selectInYearMonth(String yearPrevMonth, String yearMonth);

    @Query("SELECT * FROM withdrawal")
    public List<Withdrawal> selectAll();

    @Delete
    public void delete(Withdrawal withdrawal);

    @Update
    public void update(Withdrawal... withdrawals);

}
