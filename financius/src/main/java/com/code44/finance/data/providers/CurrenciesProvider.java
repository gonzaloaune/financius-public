package com.code44.finance.data.providers;

import android.content.ContentValues;
import android.net.Uri;

import com.code44.finance.common.model.ModelState;
import com.code44.finance.data.db.Column;
import com.code44.finance.data.db.Tables;
import com.code44.finance.money.CurrenciesManager;
import com.code44.finance.money.MoneyFormatter;

import java.util.Map;

import javax.inject.Inject;

public class CurrenciesProvider extends ModelProvider {
    @Inject CurrenciesManager currenciesManager;

    public static Uri uriCurrencies() {
        return uriModels(CurrenciesProvider.class, Tables.CurrencyFormats.TABLE_NAME);
    }

    public static Uri uriCurrency(String currencyServerId) {
        return uriModel(CurrenciesProvider.class, Tables.CurrencyFormats.TABLE_NAME, currencyServerId);
    }

    @Override protected String getModelTable() {
        return Tables.CurrencyFormats.TABLE_NAME;
    }

    @Override protected String getQueryTables(Uri uri) {
        return getModelTable();
    }

    @Override protected Column getIdColumn() {
        return Tables.CurrencyFormats.ID;
    }

    @Override protected void onAfterInsertItem(Uri uri, ContentValues values, String serverId, Map<String, Object> extras) {
        super.onAfterInsertItem(uri, values, serverId, extras);
        currenciesManager.updateFormats(getDatabase());
    }

    @Override protected void onBeforeUpdateItems(Uri uri, ContentValues values, String selection, String[] selectionArgs, Map<String, Object> outExtras) {
        super.onBeforeUpdateItems(uri, values, selection, selectionArgs, outExtras);
        throw new IllegalArgumentException("Update is not supported.");
    }

    @Override protected void onAfterUpdateItems(Uri uri, ContentValues values, String selection, String[] selectionArgs, Map<String, Object> extras) {
        super.onAfterUpdateItems(uri, values, selection, selectionArgs, extras);
        currenciesManager.updateFormats(getDatabase());
    }

    @Override protected void onAfterDeleteItems(Uri uri, String selection, String[] selectionArgs, ModelState modelState, Map<String, Object> extras) {
        super.onAfterDeleteItems(uri, selection, selectionArgs, modelState, extras);
        MoneyFormatter.invalidateCache();
    }

    @Override protected void onAfterBulkInsertItems(Uri uri, ContentValues[] valuesArray, Map<String, Object> extras) {
        super.onAfterBulkInsertItems(uri, valuesArray, extras);
        currenciesManager.updateFormats(getDatabase());
    }

    @Override protected Uri[] getOtherUrisToNotify() {
        return new Uri[]{AccountsProvider.uriAccounts(), TransactionsProvider.uriTransactions()};
    }
}
